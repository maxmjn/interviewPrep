/**
 * Created by maxmjn20 on 10/3/15.
 */

/**
 * sets OAuth header using token from URL #fragment
 */
function setOAuthHeaderForCodeReflow(){
    //Populated from server
    var access_token_from_server="$AccessToken$"; //if OAuth2 codeReflow, populate from server
//        var id_token_from_server="$jwtRawString$";
//        var refresh_token_from_server="$RefreshToken$";
//        var header_from_server="$jwtHeader$"; //header is json object doing string replace in server
//        var claims_from_server="$jwtClaims$"; //claims is json object doing string replace in server
//        var signature_from_server="$jwtSign$";

    //Look for OAuth accessToken in URL #fragment
    var params = {}, postBody = location.hash.substring(1),
        regex = /([^&=]+)=([^&]*)/g, m;
    while (m = regex.exec(postBody)) {
        params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
    }
    //if OAuth2 implicit flow
    if(params && params.access_token) {
        access_token_from_server = params.access_token;
    }
    //using regex vs. string compare so that server does not replace default value in "access_token_from_server"
    var matcher = /^\$AccessToken\$$/m;
    var hasDefault = matcher.exec(access_token_from_server);

    if(!hasDefault){
        //showHome(); //main.js
        //Add OAuth header from implicit or codeReflow methods
        //To add another header use different "key"
        //window.authorizations.add("key", new SwaggerClient.ApiKeyAuthorization("Authorization", "Bearer ".concat(access_token_from_server), "header"));
        setOIDCHeader("Bearer ".concat(access_token_from_server));//in main.js uses ajax.PreFilter to set headers
    }

};


/**
 * OAuth client Create API response data is used
 * to construct OAuth2 implicit flow URL
 * @param data
 */
function setOAuthImplicit(data){
    /*
     All URLs exposed in this method are behind firewall.
     This method shows another way to request OAuth2 access token.
     Production URLs NOT RECOMMENDED.
     This method exposed in OAuth2Admin page as its secured by login/privileges.
     */
    var elsUri_oauth = "CHANGE_ME";
    var scope_oauth = "openid profile uid memberOf user_authz:local_host";
    var response_type_oauth = "token id_token";
    var realm_oauth = "CHANGE_ME";
    var client_id_oauth = "CHANGE_ME";
    var redirect_uri = document.location.protocol + "//" + document.location.host + "/reporting-services/oauthRedirect/";

    if(document.location.hostname === 'QA CHANGE_ME'){
        realm_oauth='QA CHANGE_ME';
        elsUri_oauth = 'QA CHANGE_ME';
    }


    if(data){
        var hasRequiredFields = false;
        if(data.scopes){
            scope_oauth = data.scopes[0];
            hasRequiredFields = true;
        }

        hasRequiredFields=false;
        if(data.clientId){
            client_id_oauth = data.clientId;
            hasRequiredFields = true;
        }

        hasRequiredFields = false;
        if(data.redirectionUris){
            redirect_uri = data.redirectionUris[0];
            hasRequiredFields = true; //finally all properties available
        }

        response_type_oauth = "token";
    }

    if(hasRequiredFields){
        $('.oauth2_implicit').show();

        $('.oauth2_implicit').prop('href', elsUri_oauth.concat("/oauth2/authorize?")
                .concat("&scope=").concat(encodeURIComponent(scope_oauth))
                .concat("&response_type=").concat(encodeURIComponent(response_type_oauth))
                .concat("&realm=").concat(encodeURIComponent(realm_oauth))
                .concat("&client_id=").concat(encodeURIComponent(client_id_oauth))
                .concat("&redirect_uri=").concat(encodeURIComponent(redirect_uri))
        );

        /*
         //Requires openIdConnect user, back-end changes.
         $('.oauth2_reflow').show();
         $('.oauth2_reflow').prop('href', elsUri_oauth.concat("/oauth2/authorize?")
         .concat("&scope=").concat(encodeURIComponent(scope_oauth))
         .concat("&response_type=").concat(encodeURIComponent("code"))
         .concat("&realm=").concat(encodeURIComponent(realm_oauth))
         .concat("&client_id=").concat(encodeURIComponent(client_id_oauth))
         .concat("&redirect_uri=").concat(encodeURIComponent(redirect_uri))
         );*/
    }

};
