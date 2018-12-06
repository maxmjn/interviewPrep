/**
 * Created by maxmjn20 on 10/2/15.
 */


//without unbind/bind event gets called repeatedly
//button click
$("#search-btn").unbind("click",btnClick).bind("click",btnClick);
$("#create-btn").unbind("click",btnClick).bind("click",btnClick);
$("#update-btn").unbind("click",btnClick).bind("click",btnClick);
$("#delete-btn").unbind("click",btnClick).bind("click",btnClick);
$("#accessToken-btn").unbind("click",btnClick).bind("click",btnClick);
//focus out
$("#json-create").add("#json-update").add("#json-accessToken").unbind("focusout", prettyPrintJson).bind("focusout", prettyPrintJson);
//change
$("#json-accessToken").unbind("change",prettyPrintJson).bind("change", prettyPrintJson);
$("#json-create").unbind("change",prettyPrintJson).bind("change", prettyPrintJson);
$("#json-update").unbind("change",prettyPrintJson).bind("change", prettyPrintJson);
//initialize
$("#json-create").val('{"redirectionUris": [""],"displayName": "clientName","description": "clientDesc"}').change();
$("#json-accessToken").val('{"clientId":"", "secret":"", "scopes":[]}').change();
$("#json-update").val('{"redirectionUris": ["https://clientHost.com/callback"],"displayName": "clientName","description": "clientDesc"}').change();

function prettyPrintJson(){
    var data = $(this).val();
    data = data ? $.parseJSON(data) : ""; //json
    data = data ? JSON.stringify(data, null, ' ') : ""; //prettyPrint json
    $(this).val(data);
}

function btnClick(event)
{
    var buttonName = $(this).attr("data-name");
    var form = '#' + buttonName + '-form';

    //without unbind/bind event gets called repeatedly
    $(form).unbind("submit",formSubmit).bind("submit",formSubmit);
};

function formSubmit(event)
{
    var postData;
    var formURL = '';
    var method = '';
    var buttonName = $(this).find('button').attr('data-name');
    var loader = '#' + buttonName + '-ajaxloader';
    var highlight = '#' + buttonName + '-msg-highlight';
    var msg = '#' + buttonName + '-msg';
    var element, val;

    $(loader).show();

    switch (buttonName){
        case 'search':
            method = "GET";
            element = $(this).find("#clientId-search");
            val = element ? element.val() : '';
            postData = "?clientId=" + val;
            element = $(this).find("#displayName-search");
            val = element ? element.val() : '';
            postData = postData + "&displayName=" + val;
            formURL = config.oauth2_client_path + "search" + postData;
            break;
        case 'create':
            method = "POST";
            element = $(this).find('#json-create');
            val = element ? element.val() : '';
            postData = val ? val : '';
            element = $(this).find('#userName');
            val = element ? element.val() : '';
            formURL = config.oauth2_client_path + "create?userName=" + val;
            break;
        case 'update':
            method = "PUT";
            element = $(this).find('#json-update');
            val = element ? element.val() : '';
            postData = val ? val : '';
            element = $(this).find('#clientId-update');
            val = element ? element.val() : '';
            formURL = config.oauth2_client_path + "update?clientId=" + val;
            break;
        case 'delete':
            method = "DELETE";
            element = $(this).find('#clientId-delete');
            val = element ? element.val() : '';
            formURL = config.oauth2_client_path + "inactivate?clientId=" + val;
            break;
        case 'accessToken':
            method = "POST";
            element = $(this).find('#json-accessToken');
            val = element ? element.val() : '';
            postData = val ? val : '';
            formURL = config.oauth2_client_path + "getAccessToken";
            break;
        default:
            formURL='';
    }
    $.ajax(
        {
            url : formURL,
            type: method,
            data : postData,
            headers:{
                'Content-Type': 'application/json'
            },
            success:function(data, textStatus, jqXHR)
            {
                $(loader).hide();
                var response = JSON.stringify(data, null, ' '); //prettyPrint json

                $(highlight).removeClass("error");
                $(highlight).addClass("success");
                $(highlight).html(textStatus);
                $(msg)
                    .html('<pre><code class="prettyprint">'
                    +response
                    +'</code></pre>');

                if(!isEnvProd() && buttonName === 'create' && (data !== 'undefined' && data)){
                    setOAuthImplicit(data); //oauth2.js
                    removeOAuthDisabled();
                }


            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                $(loader).hide();

                var response = jqXHR.responseText;
                if (jqXHR.status === 200) {
                    response = $.parseJSON(response);
                    response = JSON.stringify(response, null, ' '); //prettyPrint json
                }else{
                    response = jqXHR.status + ' ' + response;
                }

                $(highlight).removeClass("success");
                $(highlight).addClass("error");
                $(highlight).html(errorThrown );
                $(msg)
                    .html('<pre><code class="prettyprint">'
                    +'<br/>'
                    + response
                    + '<br/>'
                    +'</code></pre>');
            }
        });
    event.preventDefault();	//STOP default action of form submit
}