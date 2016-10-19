#!/bin/bash

cf create-service natural_language_classifier standard NaturalLanguageClassifier

cf push
