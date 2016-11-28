'''
 Run query against Watson Discovery
'''
import requests
import json

url="https://gateway.watsonplatform.net/discovery-experimental/api/v1/environments"
environmentID="48c79dcf-c841-4b79-876f-a696ed3ac743"
collectionID="280fd6db-3b36-4944-9d30-9ffa7e94a67b"
version="2016-11-07"
user="b3402835-81f5-4416-a902-2652c51f19e5"
pwd="QZAsV0BvESk7"
# Battery collection

query="query=enriched_text.relations.subject.keywords.knowledgeGraph.typeHierarchy:battery&return=enriched_text.relations" 
url2=url+"/"+environmentID+"/collections/"+collectionID+"/query?version="+version+"&"+query

params={'version':version}
print('Let connect to Watson discovery at :' + url2)
# Use Basic authentication
try:
    r=requests.get(url2,auth=(user,pwd))
    print(r.status_code)
    
    a=json.loads(r.text)
    print(a['results'][0]['enriched_text']['relations'])
except  requests.exceptions.RequestException as e:
    print("Error"+str(e))

