var urlShortener = angular.module('urlShortener', []);

//Shortening operation handling
urlShortener.controller('shortenCtrl',
	function($scope, $http, $compile){
		//Variables
        $scope.msg = "";
        $scope.customizeMsg = "";
        $scope.domain = "";
		$scope.longUrl = "";
        $scope.shortUrl = "";

		//Request
        $scope.generateShorteningRequest =
        		function(){
        			return "http://localhost:4567/generateShortening";
        		}

        $scope.saveRequest =
        		function(){
        			return "http://localhost:4567/saveUrl?url=http://squiz.it/" + $scope.shortUrl + "&longUrl=" + $scope.longUrl;

        		}

		/*
		It send request to the server and generate a random path for the short url
		*/
        $scope.letsShortenIt =
        		function(){
        			$scope.setMessages();
                    $scope.customizeObj();

           			$http.get($scope.generateShorteningRequest())
                   		.then(function(response) {
                    		$scope.shortUrl = response.data.shortUrl;
                    	},function(response) {
                    		alert("server error for letsShortenIt() method");
                    	});
        		}

		/*
		First generate a random url and then it allow to customize it.
		*/
        $scope.setMessages =
        		function(){
        			$scope.msg = "Your short url: ";
        			$scope.domain = "http://squiz.it/";
        			$scope.customizeMsg = "Customize: ";
        		}

		/*
		Re-compile index.html adding the input text and the button for customize the url generated and save it
		*/
		$scope.customizeObj =
				function(){
                    var newDiv = document.createElement("div");
                    newDiv.id = "addedDiv"
                    newDiv.innerHTML = ' <input type="text" placeholder="Customize your short url" class="form-control" ng-model="shortUrl"> <br> <button type="submit" class="btn btn-primary btn-lg" ng-click="save()">Done &raquo;</button>  ';
                    document.getElementById("customizeForm").appendChild(newDiv);
                    $compile(newDiv)($scope);
				}

		/*
		Save the url shortened in db if it's available
		*/
        $scope.save =
        		function(){
        			$http.get($scope.saveRequest())
        				.then(function(response) {
        					var result = response.data.result;
        					var msg = response.data.urlSavedMsg;

        					if(result == "ok"){
        						var url = response.data.url;
        						prompt(msg, url);
        						$scope.reset();
        					} else if (result == "error") {
        						alert(msg);
        					}

        				},function(response) {
        					alert("server error for save() method");
        				});
        		}

		/*
		Called once the short url is been saved. Come back to the start view.
		*/
        $scope.reset =
        		function(){
        			$scope.msg = "";
                    $scope.customizeMsg = "";
                    $scope.domain = "";
                    $scope.longUrl = "";
                    $scope.shortUrl = "";

                    var addedDiv = angular.element( document.querySelector( '#addedDiv' ) );
                    addedDiv.remove();
        		}
    }
);

//Login and singup operation handling
urlShortener.controller("authCtrl",
    function($scope, $http){

        $scope.username = "";

        $scope.password = "";

		$scope.singupRequest =
				function(){
					return "http://localhost:4567/singup?username=" + $scope.username + "&password=" + $scope.password
				}

		$scope.singup =
				function(){
					$http.get($scope.singupRequest())
						.then(function(response){
                        	var result = response.data.singupResult;
                        	alert(result);
						});
				}





    }

);
	

