app.controller("userCtrl", function($scope, $http, $window, $compile){

		//Loacl scope variables
		$scope.continents;
		$scope.selectedUrl;
		$scope.detailsMsg = "Select a short url to show its details";
		$scope.labels = [];
		$scope.data = [];

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

		//Show continents statistics
		$scope.showContinentClick =
				function(shortUrl){
					$scope.selectedUrl = shortUrl;
					$scope.detailsMsg = "";

					$http.get("http://localhost:4567/showContinentClick?shortUrl=" + shortUrl)
						.then(function(response){
                        	$scope.continentList = response.data;

							//Set params for graph
							var names = [];
							var click = [];
                        	$scope.continentList.forEach(function(continent){
                        		names.push(continent.name);
                        		click.push(continent.click);
                        	});
                        	$scope.labels = names;
                        	$scope.data = click;

                        	//Resetto il contenuto
                        	var head = document.getElementById("continentHead");
                        	head.innerHTML = '';
                        	$compile(head)($scope);

                        	//e ne creo uno nuovo
                        	 var head = document.getElementById("continentHead");
                        	 var headTr = document.createElement("tr");
                        	 headTr.innerHTML = '<th>Continent</th><th>Click</th>';
                        	 head.appendChild(headTr);
                        	 $compile(head)($scope);

						}, function(response){
                        	alert("error in showContinentClick");
						});
				}

		//Show continents statistics
        $scope.showCountryClick =
        		function(continent){
        			$scope.selectedContinent = continent;
        			$http.get("http://localhost:4567/showCountryClick?shortUrl=" + $scope.selectedUrl + "&continent=" + continent)
        				.then(function(response){
                        	$scope.countryList = response.data.countryList;

                            //Resetto il contenuto
                            var head = document.getElementById("countryHead");
                            head.innerHTML = '';
                            $compile(head)($scope);

							//..e ne ricreo uno nuovo
                            var headTr = document.createElement("tr");
                            headTr.innerHTML = '<th>{{selectedContinent}} - Countries</th><th>Click</th>';
                            head.appendChild(headTr);
                            $compile(head)($scope);

                        }, function(response){
                        	alert("error in showCountryClick");
                        });
        			}

    }
);