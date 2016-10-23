import requests,json

def callOdm():
	a={'assessment': {'uid': 'string', 'customerQuery': {'firstQueryContent': 'my battery is draining', 'userId': 'bob',
	'acceptedCategory': 'battery'},
    'status': 'NEW',
    'creationDate': '2008-09-29T01:49:45.000+0000'
    }}
	url='https://brsv2-b21ca14b.ng.bluemix.net/DecisionService/rest/v1/DDRuleApp/AssessDataNeeds'
	r=requests.post(url,
	  			 json.dumps(a),
	             auth=('resAdmin', 'vf6xrhckxved'),
                      headers={'Content-Type': 'application/json'})
	return r.text
                      
if __name__ =='__main__':
	print(callOdm())