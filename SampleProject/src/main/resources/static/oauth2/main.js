/**
 * Created by maxmjn20 on 10/6/15.
 *
 * This file is loaded without security restrictions.
 * DON't mention any internal IPs/URLs/resource.
 *
 * On successful authentication, this file loads other secure resources.
 */

/**
 * function to load a given css file
 * @param href
 * @param media
 */
function loadCSS(href, media) {
    var cssLink = $("<link rel='stylesheet' type='text/css' href='"+href+ "' media='" + media + "'>");
    var el = $("head");
    el.append(cssLink);
};


/**
 * function to load a given js file
 * @param src
 */
function loadJS(src) {
    //var jsLink = $("<script type='text/javascript' src='"+src+"'>");
    //var el = $("head");
    //el.append(jsLink);
    var d = $.Deferred();
    $.getScript( src )
        .done(function( script, textStatus ) {
            //console.log( textStatus );
            d.resolve(textStatus); //response is passed on to $.when's successCallBack method - unblockUI()
        })
        .fail(function( jqXHR, settings, exception ) {
                var response = jqXHR.status + ':' + jqXHR.responseText;
                //showNotLoggedIn(true, response);
                //showMainBlock(false);
            d.reject(response); //response is passed on to $.when's failCallBack method - blockUI()
        });
    return d.promise();
};

/**
 *
 */
//function showHome(){
//    var location = document.location;
//    var port = location.port ? (':' + location.port) : '';
//    var home = location.protocol + '//' +
//        location.hostname + port + config.doc_path + "index.html";
//    $('#login-link .els').prop('href', home ).find('p').text('Home');
//}

/**
 *
 * @param elsUri
 * @param realm
 * @param username
 */
//function showLogout(elsUri, realm, username){
//
//    $('#login-link .els').prop('href', elsUri.concat('/opensso/UI/Logout?goto=')
//        .concat(encodeURIComponent(document.location)).concat(realm))
//        .find('p').text('Logout (' + username + ')');
//};

/**
 *
 */
function setExperimentDisabled(){
    var el = $('.oauth2_implicit');
    var el2 = $('.oauth2_reflow');
    el.addClass('disabled');
    el2.addClass('disabled');
};

/**
 *
 */
function removeOAuthDisabled(){
    var el = $('.oauth2_implicit');
    var el2 = $('.oauth2_reflow');
    el.removeClass('disabled');
    el2.removeClass('disabled');
};

/**
 *
 */
function isEnvProd(){
    if ((document.location.hostname === 'CHANGE_ME')){
        return true;
    } else{
        return false;
    }
}

/**
 *
 * @param show
 */
function showNotLoggedIn(show, msg){

    var el = $('#login-link').find('label');
    msg = msg ? msg : el.text();
    if(show){
        el.show().html(msg);
    } else{
        el.hide();
    }
}

/**
 *
 * @param show
 */
function showMainBlock(show){
    var el = $("#main");
 if(show){
     el.show();
 } else{
     el.hide();
 }
}

/**
 *
 * @param response
 */
function unblockUI(response){
    showNotLoggedIn(false);
    showMainBlock(true);

    //Experimental - only available in dev/localhost
    //app.js create button click calls setOAuthImplicit(). When oauthImplicit link is clicked
    //accessToken appears in URL #fragment and gets picked by setOAuthHeaderForCodeReflow() in oauth2.js
    setOAuthHeaderForCodeReflow();
}

/**
 *
 * @param response
 */
function blockUI(response){
    showNotLoggedIn(true, response);
    showMainBlock(false);
}

/**
 *
 */
function loadFiles(){

    //couldn't load on secure path because unlike JS <script> tag, <link> is not ajax
    loadCSS(config.oauth2_path + 'loader.css', 'screen');

    var location = config.oauth2_path; //un-authenticated path
    var promise = loadJS(location + 'oauth2.js'); //ajax call
    var promise2 = loadJS(location + 'app.js'); //ajax call

    location = config.oauth2Admin_path; //secure load is causing HTTP 412
    // TEST - securely load the js file
    var promise3 = loadJS(location + 'test.js'); //ajax call

    //if both secured JS loads unblockUI, on any promise fail call blockUI
    //unblockUI - also calls setOAuthHeaderForCodeReflow()
    $.when(promise, promise2).then(unblockUI, blockUI);
}

/**
*
*/
function setOIDCHeader(data){
    if (data) {
        $.ajaxPrefilter(function(options, originalOptions, jqXHR){
            jqXHR.setRequestHeader('Authorization', data);
        });
    }

};

/**
 *
 */
$(document).ready(function() {

    //since authentication is handled thru OIDC in index.html
        blockUI();
        setExperimentDisabled();
        $.ajaxSetup({
            cache: true //$.getScript() sets the cache setting to false
        });
        setOIDCHeader("Bearer " + (localStorage.accessToken || sessionStorage.accessToken)); //index.html sets accessToken
        loadFiles(); //un-blocks UI

});