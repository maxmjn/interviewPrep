//INITIALIZE
$("#json-create").val('{"redirectionUris": [""],"displayName": "clientName","description": "clientDesc"}').change();
$("#json-loadData").val('{}').change();
$("#json-update").val('{"redirectionUris": ["https://clientHost.com/callback"],"displayName": "clientName","description": "clientDesc"}').change();

//without unbind/bind event gets called repeatedly
//button click
$("#search-btn").unbind("click",btnClick).bind("click",btnClick);
$("#create-btn").unbind("click",btnClick).bind("click",btnClick);
$("#update-btn").unbind("click",btnClick).bind("click",btnClick);
$("#delete-btn").unbind("click",btnClick).bind("click",btnClick);
$("#loadData-btn").unbind("click",btnClick).bind("click",btnClick);
$("#getUsers-btn").unbind("click",btnClick).bind("click",btnClick);
//focus out
$("#json-create").add("#json-update").add("#json-loadData").unbind("focusout", prettyPrintJson).bind("focusout", prettyPrintJson);
//change
$("#json-loadData").unbind("change",prettyPrintJson).bind("change", prettyPrintJson);
$("#json-create").unbind("change",prettyPrintJson).bind("change", prettyPrintJson);
$("#json-update").unbind("change",prettyPrintJson).bind("change", prettyPrintJson);

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
    var formURL = config.apiPath + config.apiVersion + config.app;
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
            element = $(this).find("#displayName-search");
            val = element ? element.val() : '';
            formURL = formURL + config.apiUserAlbums + val;
            break;
        case 'create':
            element = $(this).find("#username-create");
            var username = element ? element.val() : '';
            element = $(this).find("#name-create");
            var name = element ? element.val() : '';
            element = $(this).find("#email-create");
            var email = element ? element.val() : '';
            element = $(this).find("#album-create");
            var albumTitle = element ? element.val() : '';
            element = $(this).find("#photo-create");
            var photoTitle = element ? element.val() : '';
            element = $(this).find("#thumbnail-create");
            var thumbnailUrl = element ? element.val() : '';
            element = $(this).find("#photourl-create");
            var photoUrl = element ? element.val() : '';
            postData = {
                           "name": name,
                           "username": username,
                           "email": email,
                           "albumTitle": albumTitle,
                           "photoTitle": photoTitle,
                           "url": photoUrl,
                           "thumbnailUrl": thumbnailUrl
                         };
            postData = JSON.stringify(postData);
            formURL = formURL + config.apiCreate;
            method = "POST";
            break;
        case 'loadData':
            method = "GET";
            //element = $(this).find('#json-loadData');
            //val = element ? element.val() : '';
            //postData = val ? val : '';
            formURL = formURL + config.apiLoadData;
            break;
        case 'getUsers':
            method = "GET";
            formURL = formURL + config.apiUsers;
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

                    switch (buttonName){
                        case 'create':
                          $(highlight).html(textStatus);
//                          $('#create-data').html('<pre><code class="prettyprint">'
//                                +response
//                                +'</code></pre>');
                         $('#create-data ul')
                          .append(
                              '<li> UserId:' + data.id + '</li>',
                              '<li> Username:' + data.username + '</li>',
                              '<li> AlbumId:' + data.albumViewList[0].id + '</li>',
                              '<li> AlbumTitle:' + data.albumViewList[0].title + '</li>',
                              '<li> PhotoId:' + data.albumViewList[0].photoViewList[0].id + '</li>',
                              '<li> PhotoTitle:' + data.albumViewList[0].photoViewList[0].title + '</li>',
                              '<li> PhotoThumbnail:' + data.albumViewList[0].photoViewList[0].thumbnailUrl + '</li>',
                              '<li> PhotoUrl:' + data.albumViewList[0].photoViewList[0].url + '</li>'
                          );
                        break;
                        case 'search':
                          $(highlight).html(textStatus);
                          var searchTable = "#search-data table";
                          $(searchTable).empty(); //clear any child before adding
                          var userId = data.id;
                          //header
                          $(searchTable)
                          .addClass("table_1")
                          .append('<tr>',
                                  '<th>User</th>',
                                  '<th>' + userId + '</th>',
                                  '</tr>'
                                  )
                          ;

                          $(searchTable)
                          .append('<tr>',
                                  '<td>' + data.username +'</td>',//each <td></td> should be single String param for table to appear properly
                                  '<td>' + data.name +','+ data.email + '</td>', //each <td></td> should be single String param for table to appear properly
                                  '</tr>'
                                  )
                          ;
                          //data rows
                          var albums = data.albumViewList;
                          $.each(albums, function( i, val ) {
                            $(searchTable)
                              .append('<tr>',
                                      '<td><a href="photos.html?userId='+ userId + '&albumId=' + val.id+ '">' + val.id + '</a></td>', //each <td></td> should be single String param for table to appear properly
                                      '<td><a href="photos.html?userId='+ userId + '&albumId=' + val.id+ '">' + val.title + '</a></td>',//each <td></td> should be single String param for table to appear properly
                                      '</tr>'
                                      )
                            ;
                          });
                          break;
                        case 'loadData':
                          $(highlight).html(textStatus);
                          //$(msg).html('<pre><code class="prettyprint">'
                          //      +response
                          //      +'</code></pre>');
                          var msgTable = "#loadData-msg table";
                          //before adding, empty child elements if any
                           $(msgTable).empty();

                          //header
                          $(msgTable)
                          .addClass("table_1")
                          .append('<tr>',
                                  '<th>Status</th>',
                                  '</tr>'
                                  )
                          ;
                          //data rows
                          $.each(data, function( i, val ) {
                            $(msgTable)
                              .append('<tr>',
                                      '<td>' + val + '</td>',
                                      '</tr>'
                                      )
                            ;
                          });
                          break;
                        case 'getUsers':
                          $(highlight).html("List of usernames");
                          var userTable = "#getUsers-list table";
                          //before adding, empty child elements if any
                          $(userTable).empty();

                          //header
                          $(userTable)
                          .addClass("table_1")
                          .append('<tr>',
                                    '<th>Id</th>',
                                    '<th>UserName</th>',
                                  '</tr>'
                                  )
                          ;
                          //data rows
                          $.each(data, function( i, val ) {
                            $(userTable)
                              .append('<tr>',
                                      '<td>' + val.id + '</td>',
                                      '<td>' + val.username + '</td>',
                                      '</tr>'
                                      )
                            ;
                          });
                          break;
                        default:
                    }//success-switch

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

function getURLParameter(sParam){

    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++){
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam){
            return sParameterName[1];
        }
    }
}

function loadPhotos(){
  var userId = getURLParameter("userId");
  var albumId = getURLParameter("albumId");
  var v1 = config.apiAlbumPhotos.replace("{userId}", userId);
  var url = v1.replace("{albumId}", albumId);
  var formURL = config.apiPath + config.apiVersion + config.app + url;

  var loader = '#photoData-ajaxloader';
  var highlight = '#photoData-msg-highlight';
  var msg = '#photoData-msg';
  $.ajax(
              {
                  url : formURL,
                  type: "GET",
                  data : "",
                  headers:{
                      'Content-Type': 'application/json'
                  },
                  success:function(data, textStatus, jqXHR)
                  {
                      $(loader).hide();
                      var response = JSON.stringify(data, null, ' '); //prettyPrint json

                      $(highlight).removeClass("error");
                      $(highlight).addClass("success");

                      var photoTable = msg + ' table';
                      $(photoTable).empty(); //clear any child before adding

                      //header
                      $(photoTable)
                      .addClass("table_1")
                      .append('<tr>',
                              '<th>Id</th>',
                              '<th>Title</th>',
                              '<th>Thumbnail</th>',//each <th></th> should be single String param for table to appear properly
                              '<th>Original</th>',
                              '</tr>'
                              )
                      ;
                      //data rows
                      var photos = data;
                      $.each(photos, function( i, val ) {
                        $(photoTable)
                          .append('<tr>',
                                  '<td>'+ val.id + '</td>', //each <td></td> should be single String param for table to appear properly
                                  '<td>'+ val.title + '</td>', //each <td></td> should be single String param for table to appear properly
                                  '<td><img src="'+ val.thumbnailUrl + '" alt="' + val.title + '"> </td>', //each <td></td> should be single String param for table to appear properly
                                  '<td><a href="'+ val.url + '">' + val.url + '</a></td>', //each <td></td> should be single String param for table to appear properly
                                  )
                        ;
                      });

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
}