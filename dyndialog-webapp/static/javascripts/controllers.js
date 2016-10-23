var theCtrls = angular.module('Ctrls', [ 'ngRoute','ui.bootstrap']);

var app = angular.module('DDApp');

var assessment={}

app.config(['$routeProvider',
     function($routeProvider) {
		// present the list of projects, and the ability to add new project
	    $routeProvider.when('/dd',
	    		{templateUrl: 'partials/Main.html',
	    		controller: 'HomeCtrl'});
	    $routeProvider.when('/dialog',
	    		{templateUrl: 'partials/Dialog.html',
	    		controller: 'DialogCtrl',
	    		resolve : {
	    			a : function(ddService) {
							return ddService.getAssessment();
						}
//					]}
	            }
	    		}
	    );
	    $routeProvider.otherwise({redirectTo: '/dd'});
    }
]);

/**
 * The first controller is supporting the home page view, with the following
 * functions:
 * - query entry form and ask button
 */
theCtrls.controller('HomeCtrl',  ['$scope','$location','ddService','$http','$sce',
      function ($scope,$location,ddService,$http,$sce) {		
			if ($scope.introduction === undefined){
				$http.get("data/intro.json").then(function(response) {
			          $scope.introduction= $sce.trustAsHtml(response.data.content);
		      },function(error) {
		    	  return [];
		      });
			}
			$scope.title="Context Driven Dialog";
	
			// when user pushes query button
			$scope.helpMe = function(query) {
				 var cq={}
				 cq.userId="bob";
				 cq.firstQueryContent=query;
				 $http({
					    method:'POST',
						url:'/dd/api/a/classify',
						data: cq,
						headers: {'Content-Type': 'application/json'}	
				 }).then(function(response){
					        ddService.setAssessment(response.data);
					        assessment=response.data;
					 		 $location.path('/dialog');
				  		},function(error) {
							alert("Query failed.");
						}); 
				 };
      }	
]);// home ctrl

theCtrls.controller('DialogCtrl',  ['$scope','$location','ddService','$http',
    function ($scope,$location,ddService,$http) {
       		$scope.title="Assessing...";
       		$scope.question=assessment.nextQuestion;
       		$scope.showButton=true;
       		$scope.response={};
    	 	if ($scope.question){
    	 		$scope.response.questionLabel=$scope.question.label;
    		}
    	 	$scope.recommendations=[]
    	 	
    	 	$scope.assess = function(r) {
    	    	var a=assessment;
    	    	a.lastResponse=r;
    	    	a.nextQuestion={}
    	    	$http({
    			    method:'POST',
    				url:'/dd/api/a',
    				data: a,
    				headers: {'Content-Type': 'application/json'}	
    	    	}).then(function(response){
    			 		assessment=response.data;
    			 		$scope.response={};
    			 		//$location.path('/dialog');
    			 		$scope.question=assessment.nextQuestion;
    		       		$scope.response.questionLabel=$scope.question.label;
    			 		if (assessment.status === "Completed") {
    			 			$scope.recommendations=assessment.recommendations;	
    			 			$scope.question.type="text";
    			 			$scope.showButton=false;
    			 		}

    		  		},function(error) {
    					alert("Query failed.");
    				}); 
    	    }
          } // constructorß                       
]);
	

