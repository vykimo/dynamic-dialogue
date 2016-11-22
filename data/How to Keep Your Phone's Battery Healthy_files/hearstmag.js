(function (w, d) {
  'use strict';
  var s1 = d.getElementsByTagName('script')[0],
  s = d.createElement('script'),
  attr,
  pageFn,
  onReady;
  s.src = '//tru.am/scripts/ta-pagesocial-sdk.js';

  attr = function (domQ, name) {
    var l = document.querySelector(domQ);
    if (l) {
      // Use getAttribute() to avoid encoded strings.
      return l.getAttribute(name);
    }
    return undefined;
  };
  pageFn = function () {
    var l, u,
        ogtype = attr('meta[property="og:type"]', 'content');
    if (w.TRUE_ANTHEM.isValidPageType(ogtype)) {
      l = document.location;
      u = l.protocol + '//' + l.host + l.pathname;
      return {
        'canonical': u,
        'og:type': 'article',
        'og:url': u
      };
    }
    return {};
  };
  onReady = function () {
    var l = document.location,
        h = l.hostname.toLowerCase(),
        cid,
        el =  d.querySelector('.article-body--source .content-source--name');
    if (h.indexOf('delish.com') > -1) {
      cid = ['257', '434'];
    } else if (h.indexOf('elledecor.com') > -1) {
      cid = ['424','590'];
    } else if (h.indexOf('esquire.com') > -1) {
      if (/^\/food-drink\//i.test(l.pathname)) {
       cid = ['434', '275','744'];
     } else if (/^\/news-politics\/politics\//i.test(l.pathname)) {
        cid = ['745'];
      } else {
        cid = ['275', '704'];
      }
    } else if (h.indexOf('goodhousekeeping.com') > -1) {
      if (/^(?:\/home\/green-living\/|\/home\/gardening\/|\/recipes\/healthy\/|\/health\/diet-plans\/|\/health\/womens-health\/|\/health\/wellness\/|\/family\/travel\/)/i.test(l.pathname)) {
        cid = ['274', '401'];
      } else if (/^\/recipes/i.test(l.pathname)) {
        cid = ['434', '401'];
      } else {
        cid = ['401'];
      }
    } else if (h.indexOf('popularmechanics.com') > -1) {
      if (/^(?:\/science\/environment\/|\/archive\/home\/improvement\/lawn-garden\/0\/10)/i.test(l.pathname)) {
        cid = ['274', '375'];
      } else {
       cid = ['375'];
      }
    } else if (h.indexOf('elle.com') > -1) {
      if (/\/beauty\//i.test(l.pathname)) {
        cid = ['258', '458'];
      } else if (/\/trend-reports\/|\/celebrity-style\//i.test(l.pathname)) {
        cid = ['315', '458'];
      } else  if (/^\/culture\//i.test(l.pathname)) {
        cid = ['433', '458'];
      } else {
        cid = ['458'];
      }
    } else if (h.indexOf('harpersbazaar.com') > -1) {
      if (/\/beauty\//i.test(l.pathname)) {
        cid = ['258', '445'];
      } else if (/\/fashion\/street-style\/|\/fashion\/fashion-articles\//i.test(l.pathname)) {
        cid = ['315', '445'];
      } else if (/^\/celebrity\//i.test(l.pathname)) {
        cid = ['433', '445'];
      } else {
        cid = ['445'];
      }
    } else if (h.indexOf('marieclaire.com') > -1) {
      if (/\/fashion\/street-style\//i.test(l.pathname)) {
        cid = ['315', '450'];
      } else if (/^\/celebrity\//i.test(l.pathname)) {
        cid = ['433', '450'];
      } else {
        cid = ['450'];
      }
    } else if (h.indexOf('cosmopolitan.com') > -1) {
      if (/\/beauty\//i.test(l.pathname)) {
        cid = ['301', '258'];
      } else if (/\/style-beauty\/fashion\//i.test(l.pathname)) {
        cid = ['301', '315'];
      } else if (/^\/food-cocktails\//i.test(l.pathname)) {
        cid = ['301', '434'];
      } else if (/^\/entertainment\//i.test(l.pathname)) {
        cid = ['301', '433'];
      } else {
        cid = ['301'];
      }
    } else if (h.indexOf('redbookmag.com') > -1) {
      cid = ['317'];
    } else if (h.indexOf('womansday.com') > -1) {
      cid = ['318'];
    } else if (h.indexOf('roadandtrack.com') > -1) {
      cid = ['319'];
    } else if (h.indexOf('countryliving.com') > -1) {
      if (/^\/cooking/i.test(l.pathname)) {
        cid = ['434', '357'];
      } else {
        cid = ['357'];
      }
    } else if (h.indexOf('seventeen.com') > -1) {
      cid = ['362'];
    } else if (h.indexOf('veranda.com') > -1) {
      cid = ['423','590','1046'];
    } else if (h.indexOf('housebeautiful.com') > -1) {
      cid = ['425','590'];
    } else if (h.indexOf('cosmopolitan.co.uk') > -1) {
      cid = ['460'];
    } else if (h.indexOf('sugarscape.com') > -1) {
      cid = ['554'];
    } else if (h.indexOf('caranddriver.com') > -1) {
      cid = ['573'];
    } else if (h.indexOf('drozthegoodlife.com') > -1) {
      cid = ['666'];
    } else if (h.indexOf('townandcountrymag.com') > -1) {
      cid = ['705'];
    } else if (h.indexOf('bestproducts.com') > -1) {
      cid = ['703'];
    } else if (h.indexOf('wearesweet.co') > -1) {
      cid = ['873'];
    } else if (h.indexOf('esquire.co.uk') > -1) {
      cid = ['1045'];
    } else if (h.indexOf('elleuk.com') > -1) {
      cid = ['1043'];
    } else if (h.indexOf('harpersbazaar.co.uk') > -1) {
      cid = ['1044'];
    } else if (h.indexOf('cosmopolitan.co.uk') > -1) {
      cid = ['1042'];
    }
    if (cid) {
      if (el && (/The Mix/i.test(el.textContent))){
        cid.push('647');
      }
     w.TRUE_ANTHEM.configure(cid, {page: pageFn});

    }
  };
  if (s.addEventListener) {
    s.addEventListener('load', onReady, false);
  } else {
    s.onreadystatechange = function () {
      if (s.readyState in {loaded: 1, complete: 1}) {
        s.onreadystatechange = null;
        onReady();
      }
    };
  }
  s1.parentNode.insertBefore(s, s1);
}(window, document));
