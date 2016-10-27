'''
Front end for NLC transparent to the NLC used
'''
# Import Watson Service SDKs
from watson_developer_cloud import NaturalLanguageClassifierV1 as NLC

class NLClassifier(object):
	
	def __init__(self,username,password,classifierId):
		# Setup Watson SDK
		self.natural_language_classifier = NLC(username=username,password=password)
		self.classifierId=classifierId
	
	
	def classify(self,text):
		return self.natural_language_classifier.classify(self.classifierId, text)   

	

