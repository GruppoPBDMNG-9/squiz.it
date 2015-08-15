var urlShortener = angular.module('urlShortener', []);

//Shortening operation handling
urlShortener.controller('shortenCtrl',
	function($scope, $http){

		$scope.longUrl = "";
        $scope.errorMsg = "errorMsg";
        $scope.shortUrl = "shortUrl";

        $scope.shortening =
        	function(longUrl){
           		$http.get("http://localhost:4567/shortening", {params:{longUrl: longUrl}})
                   	.success(function(response){
                   		$scope.errorMsg = "success"
                   	})
                   	.error(function(response){
                   		$scope.errorMsg = "error";
                   	})
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

                	})
                	.error(function(response){

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
	

