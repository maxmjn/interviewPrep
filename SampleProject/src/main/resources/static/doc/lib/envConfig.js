'use strict';
window.envTDS = {
        oAuthClientId: '@oAuthClientId@',
        oAuthRealm: 'aolcorporate/aolexternals',
        oAuthServerUrl: '@oAuthServerUrl@',
        oAuthSwaggerRedirectUri: '@oAuthRedirectUri@'

};

// https://targeting-data-service-api-qa.mse.aolp-dev.aolcloud.net/
// 375e9b95-947d-4f36-a302-8590f46cf332

// ========== load styleguide based on environment ========== //
var link = document.createElement('link');
if(window.envTMS != undefined){
  link.href = window.envTMS.styleGuideCssUrl;
}
link.rel = 'stylesheet';
document.getElementsByTagName('head')[0].appendChild(link);