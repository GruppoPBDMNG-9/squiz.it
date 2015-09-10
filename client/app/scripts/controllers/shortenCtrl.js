var urlShortener = angular.module('urlShortener', []);

//Shortening operation handling
urlShortener.controller('shortenCtrl',
	function($scope, $http, $compile, $window, $location){
		//Variables
        $scope.msg = "";
        $scope.customizeMsg = "";
        $scope.domain = "";
		$scope.longUrl = "";
        $scope.shortUrl = "";
        $scope.username = "";
        $scope.password = "";


		/*
		Function called on page load
		*/
		$scope.init =
        		function(){
        			sessionStorage.username = "";
        			sessionStorage.loggedIn = false;
        		}

		//Shortening Request
        $scope.generateShorteningRequest =
        		function(){
        			return "http://localhost:4567/generateShortening";
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
        Formalize request to send to the server
        */
        $scope.saveRequest =
        	function(){
        		var user;
        		if(sessionStorage.loggedIn === "false"){
        			user = "---";    //username allow only numbers and letters. so this is a good solution
        		} else {
        			user = sessionStorage.username;
        		}

        		return "http://localhost:4567/saveUrl?url=http://squiz.it/" + $scope.shortUrl
                										+ "&longUrl=" + $scope.longUrl
                										+ "&username=" + user;
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

        		//Singup
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
                                	$scope.username = "";
                                	$scope.password = "";
        						}, function(response){
        							alert("error in singup");
        						});
        				}

        		//Login
            	$scope.loginRequest =
            			function(){
            				return "http://localhost:4567/login?username=" + $scope.username + "&password=" + $scope.password
            			}

            	$scope.login =
            			function() {
                            $http.get($scope.loginRequest())
                            	.then(function(response){
                            		var result = response.data.result;
                            		if(result == "error"){
                            			alert("Invalid username or password");
                            		} else if (result == "singinError") {
                                    	alert("Disallowerd username or password");
                            		} else {
                            			var username = response.data.username;

										/*
										Lo salvo sia in sessionStorage per farlo sopravvivere agli aggiornamenti e sia in $scope.loggedIn per controllare che ci sia un utente loggato
										quando viene effettuato uno shortening. Se non c'� bisogna salvare lo shortening nel database come shortening effettuato da un host.
										*/
                            			sessionStorage.loggedIn = true;
                            			$scope.sessionStorage = sessionStorage.loggedIn;

										/*
										Salvo l'username nello session storage per sopravvivere ai refresh
										*/
                            			sessionStorage.username = username;

                            			/*
                            			Mi trasferisco sulla pagina in cui si � autenticati. Tutte le variabili dovrebbero essere visibili
                            			*/
                            			$window.location.href = ("/squiz/client/app/account.html");
                            		}
                            	}, function(response){
                            		alert("Some errors occurred during login");
                            	});
            			}
    }
);

//USER CTRL
urlShortener.controller("userCtrl",
    function($scope, $http, $window){

		//Loacl scope variables
		$scope.continents;

		//Account init
		$scope.accountInit =
				function(){
					//check
					var loggedIn = sessionStorage.loggedIn;
                    if(loggedIn==="true"){
                    	$scope.username = sessionStorage.username;
                    } else {
                    	alert("session expired");
                    	$window.location.href = ("/squiz/client/app/index.html");
                    }

                    //Load Statistics
                    $scope.loadShortening();
				}

		//Logout
    	$scope.logout =
    			function(){
    				sessionStorage.username = "";
    				sessionStorage.loggedIn = false;
    				$window.location.href = ("/squiz/client/app/index.html");
    			}

		//Load statistics
		$scope.loadShortening =
				function(){
					$http.get("http://localhost:4567/loadShortening?username=" + $scope.username)
						.then(function(response){
							$scope.totalShorteners = response.data.totalShorteners
                        	$scope.totalClick = response.data.totalClick;
                        	$scope.records = response.data.records;
						}, function(response){
                        	alert("error in loadShortening");
                        });
				}

		//Show more statistics
		$scope.showStat =
				function(shortUrl){
					$http.get("http://localhost:4567/showUrlStat?shortUrl=" + shortUrl)
						.then(function(response){
                        	var geoloc = response.data.geolocStat;
                        	var continentNames = [];

                        	angular.forEach(geoloc, function(value){
                        		this.push(value);     //TESTARE SE COS� FUNZIONA
                        	}, continentsArray);

							$scope.continents = continentsArray;

						}, function(response){
                        	alert("shit");
						});
				}
    }
);