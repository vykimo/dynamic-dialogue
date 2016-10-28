'''
Load device problem training set to train 
watson classifier

Steps:
- create a classifier in bluemix
'''

import json
import requests
from watson_developer_cloud import NaturalLanguageClassifierV1 as NaturalLanguageClassifier


def listClassifiers(natural_language_classifier):
    ''' Retrieves the list of classifiers for the service instance. 
    Returns an empty array if no classifiers are available. 
    '''
    classifiers = natural_language_classifier.list()
    return classifiers


def buildNLC(credentials):
    return NaturalLanguageClassifier(
                                username=credentials['credentials']['username'],
                                password=credentials['credentials']['password'])


def getClassifierStatus(natural_language_classifier,cid):
    status = natural_language_classifier.status(cid)
    return  status['status']

def classify(natural_language_classifier,cid,text):
    classes = natural_language_classifier.classify(cid, text)   
    return classes

def trainAsCSV():
    ''' provide training data to watson using csv: text,class
    '''
    trainSet=[]
    f=open('./data/device-trainSet.csv','r')
    for line in f:
        record=line.split(',')
        r={}
        r['text']=record[0].strip()
        r['classes']=record[1].strip()
        trainSet.append(r)
    f.close()
    return trainSet

def classifyByHttp(credentials,str):
    ''' Returns the predicted class for the given string
    '''
    r = requests.post(credentials['credentials']['url']+"/"+credentials['credentials']['classifierId']+"/classify",
                      json.dumps({'text':aQuery}),
                      auth=(credentials['credentials']['username'], credentials['credentials']['password']),
                      headers={'Content-Type': 'application/json'})
    return r;
    
if __name__ == "__main__":
    print("Start")
    
    f=open('./data/nlc-credentials.json','r')
    credentials=json.load(f)
    nlc=buildNLC(credentials)
    classifiers=listClassifiers(nlc)
    fc=classifiers['classifiers'][0]
    
    print('Classifier is:'+getClassifierStatus(nlc,fc['classifier_id']))
    
    test=trainAsCSV()
    [print(x) for x in test]
    # Classify each test observation and store its prediction and label
    predictionsAndLabels = map(lambda o:  (classifyByHttp(credentials,o['text']).json(), o['classes']), test)
    category=classify(nlc, fc['classifier_id'],"my battery is not holding charge")
    print(category['top_class']+" with "+str(category['classes'][0]))
  
    '''
    category=classify(nlc, fc['classifier_id'],"my iphone did not keep charge")
    print(category['top_class']+" with "+str(category['classes'][0]))
    category=classify(nlc, fc['classifier_id'],"my battery is not holding charge")
    print(category['top_class']+" with "+str(category['classes'][0]))
    '''
  
