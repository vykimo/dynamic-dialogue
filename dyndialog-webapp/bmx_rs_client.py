import os,requests,json

'''

'''
class RuleServiceClient(object):

	def __init__(self):
		f=open('./bmx-rs-credentials.json','r')
		credentials=json.load(f)
		self.user=credentials['credentials']['username']
		self.pwd=credentials['credentials']['password']
		self.url=credentials['credentials']['url']
		self.dataNeedRuleSet=credentials['credentials']['dataNeedRuleSet']
		self.dialogRuleSet=credentials['credentials']['dialogRuleSet']

		
	def assessDataNeed(self,assessment):
                request={'assessment':assessment}
                response=requests.post(self.url+"/"+self.dataNeedRuleSet,json.dumps(request), auth=(self.user, self.pwd),headers={'Content-Type': 'application/json'})
                aOut=json.loads(response.text)
                return aOut['assessment']
		
	def processQuestion(self,assessment):
                request={'assessment':assessment}
                response=requests.post(self.url+"/"+self.dialogRuleSet,json.dumps(request),auth=(self.user, self.pwd),headers={'Content-Type': 'application/json'})
                aOut=json.loads(response.text)
                return aOut['assessment']
                      
if __name__ =='__main__':
	a={'uid': 'string', 'customerQuery': {'firstQueryContent': 'my battery is draining', 'userId': 'bob',
		'acceptedCategory': 'battery'},
	    'status': 'NEW',
	    'creationDate': '2016-09-29T01:49:45.000+0000'
	    }
	rs=RuleServiceClient();
	print(rs.assessDataNeed(a))
