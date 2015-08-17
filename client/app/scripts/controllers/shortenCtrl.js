var urlShortener = angular.module('urlShortener', []);

//Shortening operation handling
urlShortener.controller('shortenCtrl',
	function($scope, $http){
		$scope.longUrl = "";
        $scope.errorMsg = "errorMsg";
        $scope.shortUrl = "shortUrl";

        $scope.shortening =
        		function(){
           			$http.get("http://localhost:4567/shortening?longUrl=" + $scope.longUrl)
                   		.then(function(response) {
                    		$scope.shortUrl = response.data.shortUrl;
                    		alert("ok");
                    	},function(response) {
                    		alert("shortening error");
                    	});
        	}
    }
);

//Login and singin operation handling
urlShortener.controller("authCtrl",
    function($scope, $http){
        $scope.email;
        $scope.password;

        $scope.login =
        	function(email, password){
            	$http.get(	"http://localhost:4567/login",
            				{params:{
            					email: email,
            					password: password
            					}
            				})
                	.success(function(response){
                		alert("succes");
                	})
                	.error(function(response){
                		alert("Se visualizzi questo messaggio significa che non funziona NIENTE DI NIENTE come sempre.");
                	})
        	}

        /*$scope.login =
                       	function(email, password){
                       		$http.get(	"http://localhost:4567/login",
                       					{params:{
                       						email: email,
                       						password: password
                       						}
                       					}
                       				).success(function(response){

                       				})
                       				.error(function(response){

                       				})
                       															}})
                       	}*/

    }

);
	

