/**
 * The Modal module provides generic support for fullscreen modal popups.
 *
 * Base markup for a generic modal:
 *
 * <div class="modal-overlay">
 *     <div id="..." class="modal">
 *         <header class="modal-header">
 *             ...
 *             <button class="modal-close"><i class="icon icon-close01"></i></button>
 *         </header>
 *         <div class="modal-content">...</div>
 *     </div>
 * </div>
 *
 * You can set an additional 'header-inverse' class on the 'modal-header' element to have
 * the header in an inverse (i.e. darker) color.
 */
define("modules/Modal",

	["jquery", "modules/Debounce", "modules/Video"],

	function ( $, Debounce, Video ) {
		"use strict";

		/**
		 * Initialize Modal object, setting common variables and attaching events.
		 */
		var Modal = function ( el ) {
			// Common variables.
			this.el = $( el );
			this.overlay = this.el.parents(".modal-overlay");
			this.$html = $("html");
			this.$body = $("body");
			this.$window = $( window );
			this.bodyOffset = 0;

			var $modalTriggers = $(".js-modal-show[data-modal-id='" + this.el.attr("id") + "']");

			// Show modal when clicking on elements with the 'js-modal-show' class and a 'data-modal-id'
			// attribute set to the modal id attribute value.
			$modalTriggers.on("click", function ( e ) {
				this.el.trigger("show.app.modal");

				var $target = $( e.target ).closest(".js-modal-show");

				// omniture tracking
				$.event.trigger( {
					type: "render.app.modal-trigger-click",
					item: $target
				} ) ;

				// if element has autopopulate class
				// populate modal element content from matching data attributes
				if ( $target.hasClass("js-modal-autopopulate") ) {
					$(".js-modal-show").removeClass("active-trigger");
					$target.addClass("active-trigger");
					Modal.autopopulate( $target );
				}
			}.bind( this ) );

			//add prev/next functionality to cycle through modal triggers in group
			// and populate data from current trigger

			this.el.on("click", ".modal-dir", function ( e ) {
				var $target = $( e.target );

				if ( $target.hasClass("modal-prev") ) {
					Modal.navigate("prev");
				} else if ( $target.hasClass("modal-next") ) {
					Modal.navigate("next");
				}
			}.bind( this ) );

			// Close modal when clicking on child elements with the 'js-modal-hide' class set.
			this.el.find(".js-modal-hide").on("click", function () {
				this.el.trigger("hide.app.modal");
			}.bind( this ) );

			// Hide modals when clicking on overlay or close button.
			this.overlay.on("click", function ( e ) {
				var eventTarget = $( e.target );
				if ( e.target === this.overlay.get( 0 ) || eventTarget.closest(".modal-close").length > 0 ||
					eventTarget.closest(".modal-filter--cancel").length > 0 ) {
					this.el.trigger("hide.app.modal");
				}
			}.bind( this ) );

			// Show modals when triggering the 'show.app.modal' event on the modal element.
			this.el.on("show.app.modal", function () {
				this.show();
			}.bind( this ) );

			// Hide modals when triggering the 'hide.app.modal' event on the modal element.
			this.el.on("hide.app.modal", function () {
				this.hide();
				$(".js-modal-clear").empty();
			}.bind( this ) );

			// Center modals when triggering the 'center.app.modal' event on the modal element.
			this.el.on("center.app.modal", function () {
				this.center();
			}.bind( this ) );

			// Modal position responds to window resize events.
			this.$window.on("resize", function () {

				var windowWidth = this.$window[0].innerWidth;

				if ( this.overlay.width() !== windowWidth ) {
					this.overlay.width( windowWidth );
				}

				this.center();
			}.bind( this ) );

			// Center modal on page.
			this.center();
		};

		/**
		 * Show modal with overlay on page.
		 */
		Modal.prototype.show = function () {
			this.$html.addClass("no-scroll");
			$(".js-modal-show[data-modal-id='" + this.el.attr("id") + "']").addClass("modal-active-triggers");
			this.el.addClass("modal-active");

			this.overlay.fadeIn( 300, function() {

				if (this.$html.hasClass("touch") ) {
					this.bodyOffset = this.$body.scrollTop();
					this.$body.css("position", "fixed");
				}
			}.bind( this ) );
		};

		/**
		 * Hide modal and overlay from page.
		 */
		Modal.prototype.hide = function () {
			this.overlay.fadeOut( 300 );
			this.$body.css("overflow", "visible").css("overflow-x", "hidden");
			this.$html.removeClass("no-scroll");
			$(".modal-active-triggers").removeClass("modal-active-triggers");
			this.el.removeClass("modal-active");

			if (this.$html.hasClass("touch") ) {
				this.$body.css("position", "relative");
				this.$window.scrollTo( this.bodyOffset );
			}
		};

		/**
		 * Center modal on page horizontally and vertically.
		 */
		Modal.prototype.center = function () {

			if ( this.el.hasClass("modal-autoposition") ) {
				return;
			}

			var hidden = this.overlay.is(":hidden");

			if ( hidden ) {
				this.overlay.show();
			}

			var w = this.el.width(), h = this.el.height();

			if ( hidden ) {
				this.overlay.hide();
			}

			if ( w > 0 && h > 0 ) {
				var topOffset  = ( this.$window.height() - h ) / 2, leftOffset = ( this.$window.width() - w ) / 2;
				this.el.animate( {"left" : leftOffset + "px", "top" : topOffset + "px"}, 200 );
			}
		};

		/**
		 * Previous and next navigation for modal
		 */
		Modal.navigate = function ( dir ) {
			if ( ! $(".modal-active").hasClass("modal-navigable") ) {
				return;
			}
			var $triggers = $(".modal-active-triggers"),
				$currTrigger = $triggers.filter(".active-trigger"),
				$newTrigger;

			if ( dir === "prev") {
				$newTrigger = ( $currTrigger.index() === 0 ) ? $triggers.last() : $currTrigger.prev();
			} else if ( dir === "next") {
				$newTrigger = ( $currTrigger.index() === $triggers.length - 1 ) ? $triggers.first() : $currTrigger.next();
			}

			$triggers.removeClass("active-trigger");
			$newTrigger.addClass("active-trigger");
			Modal.autopopulate( $newTrigger );
		};

		/**
		 * Populate modal from content in data attributes.
		 */
		Modal.autopopulate = function ( $target ) {
			var data;
			// get the modal data from the element that triggered the modal
			try {
				data = $.parseJSON( $target.attr("data-modal-content") );
			} catch (e) {
				console.warn(e);
			}
			// get the modal specified by that element
			var $modal = $( "#" + $target.attr("data-modal-id") ),
				modalType = data["modal-type"],
				$modalShare = $modal.find(".modal-image-share"),
				$modalTargetShare = $target.find(".modal-thumb-share"),
				$modalImage = $modal.find(".modal-image"),
				$modalVideo = $modal.find(".modal-video"),
				$modalVideoFrame;

			if ( !$modal.length ) {
				return;
			}

			for ( var key in data ) {
				var $modalEl = $modal.find( "." + key );
				if ( data.hasOwnProperty( key ) && data[key].dest && $modalEl.length ) {
					// get the element in the modal with a class matching the current key

					switch( data[key].dest) {
						case "html":
							$modalEl.empty();
							// if the content is specified in the data, populate from that
							if ( data[key].content ) {
								$modalEl.html( data[key].content );
							// if the content is not specified in the data, pull it from the matching data attribute
							// Note: This ensures that html such as links will be rendered correctly
							} else {
								var markup = $("<div />").html( $target.attr("data-" + key ) ).html();
								$modalEl.html( markup );
							}
							break;
						case "class":
							$modalEl.removeClass( data[key].remove ).addClass( data[key].content );
							break;
						default:
							$modalEl.attr( data[key].dest, data[key].content );
					}
				}
			}

			if ( modalType === "modal-type-video") {
				$modalImage.attr("src", "");
				$modalVideoFrame = $modalVideo.find("iframe");
				$modalVideoFrame.attr("src", $modalVideoFrame.data("src") )
												.attr("data-autoplay", "true")
												.video( { container: ".modal-content-container" }, true );

				$modalVideoFrame.on("videoReady", function( event, vid, type) {

					if ( type === "gopher" ) {
						if ( $modalVideoFrame.data("autoplay") ) {
							Video.getPlayerById( $modalVideoFrame.attr("id") ).playVideo();
						}

						$(".modal-content-container").on("scroll", function() {
							Video.handleScroll();
						} );
					}

				} );

			} else {

				$modalVideo.empty();

				if ( $modalTargetShare.length ) {
					$modalShare.empty();
					// Clone share button from modal trigger into modal
					$modalTargetShare.clone().appendTo( $modalShare );
				}
			}
		};

		/**
		 * Keyboard Navigation for Navigable Modals
		 * left arrow = previous, right arrow = next
		 */
		$( document ).on("keydown", function ( e ) {
			if ( e.keyCode === 37 ) {
				Modal.navigate("prev");
			} else if ( e.keyCode === 39 ) {
				Modal.navigate("next");
			}
		} );

		return {

			/**
			 * init
			 * @param el - optional - specifies a specific element to initialize
			 * @returns {Modal}
			 */
			init: function ( el ) {
				var $modal = $(".modal");

				// initialize a modal on a specific element
				if ( typeof el !== "undefined") {
					return new Modal( el );
				}

				if ( ! $modal.length ) {
					return;
				}
				// initialize on all containers with .modal
				$modal.each( function ( i, el ) {
					new Modal( el );
				} );

			}
		};
	}
);
