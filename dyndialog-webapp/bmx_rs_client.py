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

		
	def assessDataNeed(self,assessment):
		response=requests.post(self.url+"/"+self.dataNeedRuleSet,
	  			    json.dumps(assessment),
	                auth=(self.user, self.pwd),
                    headers={'Content-Type': 'application/json'})
		return response.text
		

                      
if __name__ =='__main__':
	a={'assessment': {'uid': 'string', 'customerQuery': {'firstQueryContent': 'my battery is draining', 'userId': 'bob',
		'acceptedCategory': 'battery'},
	    'status': 'NEW',
	    'creationDate': '2008-09-29T01:49:45.000+0000'
	    }}
	rs=RuleServiceClient();
	print(rs.assessDataNeed(a))