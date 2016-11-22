/**
 * Video
 */

/*global YT:false*/

define("modules/Video",

	["jquery", "modules/Debounce"],

	function( $, Debounce ) {
		"use strict";

		var Video = ( function() {

			var toLoad = [],
				players = {},
				playing = false,
				playerState = {
					UNSTARTED: -1,
					ENDED: 0,
					PLAYING: 1,
					PAUSED: 2,
					BUFFERING: 3,
					CUED: 5
				};

			/**
			 * Register players from array when API is ready
			 */
			function registerPlayers() {
				for ( var i = 0, l = toLoad.length; i < l; i++ ) {
					Video.registerPlayer( toLoad[ i ], "youtube" );
				}
				toLoad = [];
			}

			/**
			 * Handler for player state change
			 * @param  {evt} e Youtube event
			 */
			function onPlayerStateChange( e ) {
				var stateName = "state",
					data = e.data,
					target = e.target,
					videoId = target.getVideoData()["video_id"],
					id = target.id,
					player = players[ id ];

				switch ( data ) {
					case playerState.PLAYING:
						stateName = "play";
						stopOtherPlayers( id );
						break;
					case playerState.PAUSED:
						stateName = "pause";
						player.pausedInview = player.inview;
						break;
					case playerState.ENDED:
						stateName = "ended";
						break;
				}

				playing = data === playerState.PLAYING ? true : false;

				// Trigger event ( play, pause, ended )
				$( window ).trigger({
					type: "render.app.video." + stateName,
					videoId: videoId,
					id: id
				});
			}

			/**
			 * Handler to pause players when one starts playing
			 * @param  {string} currPlayerId Player id
			 */
			function stopOtherPlayers( currPlayerId ) {
				for ( var play in players ) {
					var player = players[ play ];
					// check if video is playing
					if ( player.id !== currPlayerId && player.getPlayerState() === playerState.PLAYING ) {
						player.pauseVideo();
					}
				}
			}

			return {
				/**
				 * init
				 */
				init: function() {
					/**
					 * Video Instance plugin
					 * @param  {obj} options   Player options
					 * @param  {bool} overwrite Overwrite flag
					 * @return {obj}           Player instance object
					 */
					$.fn.video = function video( options, overwrite ) {
						var src = this.attr("src") || this.attr("data-src"),
							type = src.indexOf("youtube") >= 0 ? "youtube" : "gopher";
						return Video.registerPlayer( this, type, options, overwrite );
					};

					// Add global method when Youtube API is ready
					window.onYouTubeIframeAPIReady = registerPlayers;

					// gopher events listener
					window.addEventListener( "message", this.gopherEvents );

					// scroll event
					Debounce.on("scroll", "videos", this.handleScroll.bind( this ) );
				},

				/**
				 * Scroll handler to play/pause videos in/out of view
				 */
				handleScroll: function() {
					var range = 0.3;
					for ( var play in players ) {
						var player = players[ play ],
							iframe = player.getIframe(),
							visibility = this.calculateVisibility( iframe, player.container ),
							state;

						// if a range is provided, overwrite default
						if ( player.range ) {
							range = player.range;
						}

						try {
							state = player.getPlayerState();
						}catch(err){}

						if ( visibility > range ) {
							this.playerInView( player, state, iframe.dataset.autoplay );
						} else {
							this.playerOutView( player, state );
						}
					}
				},

				/**
				 * Calculates if element is visble within a container/window
				 * @param  {iframe} video     Iframe element
				 * @param  {div} container Optional container, default is window
				 * @return {int}           Visible value of element
				 */
				calculateVisibility: function( video, container ) {
					var x = 0,
						y = 0,
						w = video.offsetWidth,
						h = video.offsetHeight,
						r, //right
						b, //bottom
						visibleX, visibleY,
						$container,
						parent = video;

					$container = ! container ? $( window ) : $ ( container );

					while ( parent && parent !== document.body ) {
						x += parent.offsetLeft;
						y += parent.offsetTop;
						parent = parent.offsetParent;
					}

					r = x + w;
					b = y + h;

					visibleX = Math.max(0, Math.min( w, $container.scrollLeft() +
						$container.height() - x, r - $container.scrollLeft() ) );
					visibleY = Math.max(0, Math.min( h, $container.scrollTop() +
						$container.height() - y, b - $container.scrollTop() ) );

					return visibleX * visibleY / ( w * h );
				},

				/**
				 * In view player handler
				 * @param  {obj} player   Player object
				 * @param  {int} state    Player state
				 * @param  {bool} autoplay Autoplay flag
				 */
				playerInView: function( player, state, autoplay ) {
					var inviewPlaying = ! player.inview ||
						( state !== playerState.PLAYING && state !== playerState.ENDED );
					// play if autoplay is on and player was not in view or if in view but has been paused
					// if the playing flag is on, another video in view is playing
					// if the video was paused in view, don't autoplay when it's back in view
					if ( autoplay && inviewPlaying && ! playing && ! player.pausedInview ) {
						player.playVideo();
					}
					player.inview = true;
				},

				/**
				 * Out of view player handler
				 * @param  {obj} player Player object
				 * @param  {int} state  Player state
				 */
				playerOutView: function( player, state ) {
					if ( state === playerState.PLAYING ) {
						try {
							player.pauseVideo();
						}catch(err){}
					} else if ( state === playerState.PAUSED && player.inview ) {
						player.pausedInview = true;
					}
					player.inview = false;
				},

				/**
				 * Checks api state and pushes players to array for when is ready
				 * @param  {jquery} el Player element
				 * @return {bool}    True if it is loaded and ready
				 */
				checkYT: function( el ) {
					var result = false;

					if ( window.YT && YT.loaded ) {
						result = true;
					} else {
						toLoad.push( el );
					}

					return result;
				},

				/**
				 * Gopher player instance
				 * @param  {string} id      Video id
				 * @param  {obj} options Options object
				 * @return {obj}         Gopher player
				 */
				gopherPlayer: function( id, options ) {
					var player = (function(){
						var el = document.getElementById( id ),
							state = playerState.UNSTARTED;
						return {
							stateChange: options.events.onStateChange,
							getIframe: function(){
								return el;
							},
							getVideoData: function() {
								return {
									"video_id" : el.dataset.id
								};
							},
							setPlayerState: function( num ) {
								state = num;
							},
							getPlayerState: function() {
								return state;
							},
							playback: function( state ) {
								var payload = "hdsPlayerPlayback:::" + state;
								if ( players.hasOwnProperty( id ) && players[ id ].ready ) {
									el.contentWindow.postMessage( payload,"*" );
								}
							},
							playVideo: function() {
								this.playback("play");
							},
							pauseVideo: function() {
								this.playback("pause");
							}
						};
					})();
					return player;
				},

				/**
				 * Gopher events listener
				 * @param  {event} event Window event
				 */
				gopherEvents: function( event ) {
					// Filter messages by origin
					if ( event.origin.indexOf("hearstdigitalstudios") >= 0 ) {
						var dataArr = event.data.split(":::"),
							type = dataArr[0],
							id = dataArr[1],
							player,
							events = {
								hdsPlayerReadyState: true,
								hdsPlayerPlayBackComplete: playerState.ENDED,
								hdsPlayerPlayState: playerState.PLAYING,
								hdsPlayerPauseState: playerState.PAUSED
							};
						// If type is an event, fire state change callback
						if ( type in events ) {
							var $player = $("[data-id='" + id + "']");
							player = $player.video();
							// check that player has been loaded first
							if ( type === "hdsPlayerReadyState" ) {
								player.ready = true;
								$player.trigger("videoReady", [ id, "gopher" ] );
							} else if ( player.ready ) {
								player.setPlayerState( events[type] );
								player.stateChange({
									data: events[type],
									target: player
								});
							}
						}
					}
				},

				/**
				 * Returns player by id
				 * @param  {string} id       Player id
				 */
				getPlayerById: function( id ) {
					return players[id];
				},

				/**
				 * Checks api and register a player
				 * @param  {jquery} e       Player element
				 * @param {string} type Player type ( youtube, gopher )
				 * @param  {obj} options Player options
				 * @param {bool} overwrite Overwrites a new player instance
				 */
				registerPlayer: function( e, type, options, overwrite ) {
					var el = e,
						Player,
						opts,
						pl,
						id,
						defaults = {
							events: {
								onStateChange: onPlayerStateChange
							}
						};

					// check element and api if youtube
					if ( ! el || ( type === "youtube" && ! this.checkYT( el ) ) ) {
						return;
					}

					// if element does not have an id, create unique one
					if ( ! el.attr("id") ) {
						el.attr("id", "player_" + new Date().getTime() );
					}

					// select player, id and options
					Player = type === "youtube" ? YT.Player : this.gopherPlayer;
					id = el.attr("id");
					opts = ! options ? defaults : $.extend( defaults, options );

					if ( ! players[ id ] || overwrite ) {
						pl = $.extend( new Player( id, opts ), opts, { id : id } );
					} else {
						if ( options ) {
							pl = $.extend( players[ id ], options );
						}
					}

					if ( pl ) {
						players[ id ] = pl;
					}

					return players[ id ];
				}

			};

		})();

		return Video;
	}
);
