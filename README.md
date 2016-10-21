# Workload - Context Driven Dialog


###Scalable Web Application Example Implemented in Cloud Foundry

This application demonstrates how to combine **IBM Watson™ Natural Language Classifier** and **Retrieve and Rank** with a rule-based system to offer a context driven conversation solution, full flexibility in terms of code change, empowering the business user to change the conversation flow and recommendations. To learn basic concepts, see [Cognitive concepts 101](https://developer.ibm.com/cloudarchitecture/docs/cognitive-concepts-101/).


----


## Introduction

A sample application has been created so you can deploy it into your personal space 
after signing up for Bluemix. You will attach the **IBM Watson™ Natural Language Classifier** and 
**Retrieve and Rank** services to the application as well as learn to begin using these services.

## What Does the App Do?
**TODO JEROME EXPLAIN APP**

## Sign up for / Log into Bluemix and DevOps

Sign up for Bluemix at https://console.ng.bluemix.net and DevOps Services at https://hub.jazz.net.
When you sign up, you'll create an IBM ID, create an alias, and register with Bluemix.


The application can be deployed in two ways:
Single click deploy via DevOps Services
Manual push through Cloud Foundry command line client


## Single click deploy via DevOps Services
https://new-console.ng.bluemix.net/docs/develop/deploy_button.html

**TODO CREATE BUTTON**

[![Deploy to Bluemix](https://bluemix.net/deploy/button.png)](https://bluemix.net/deploy?repository=https://github.com/hassenius/dynamic-dialogue)

Enjoy! (note, it may take minute or so for the app to start)


## Manual push through Cloud Foundry commmand line client

The goal is to download the application code from github and push it to Bluemix Runtimes to create a running
application that is bound to and consuming Watson services.

If you do not already have the Cloud Foundry command line client installed an configured, you will now need to 
follow the [instructions here](https://github.com/cloudfoundry/cli)

A reference guide can be found [here](https://new-console.ng.bluemix.net/docs/cli/reference/cfcommands/index.html)

**TODO** update the link above to list out the api endpoints for each region


1. Download the application code

     If you have git installed, you can clone the repository

     ```git clone **TODO INSERT URL**```

     or download and extract the [Zip file](https://github.com/hassenius/docs/archive/master.zip)
     
1. Create the services using the **Cloud Foundry command line tool** 

     ```cf create-service natural_language_classifier standard NaturalLanguageClassifier```
     
1. Push the Python application

     ```cf push```

     This will push the application as specified in the manifest.yaml file
     
     When the push is completed the application details will be reported, including the randomly generated URL. 
     Navigate to this URL to view and use the application.
    



