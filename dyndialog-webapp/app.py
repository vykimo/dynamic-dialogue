import os, json, requests
from flask import Flask,request,jsonify
from datetime import date
import BatteryProcessing

from  NLCclient import NLClassifier

# When deploying to Bluemix Watson Service Credentials of bound services are available in VCAP_SERVICES
if os.environ.get('VCAP_SERVICES'):
    services = json.loads(os.environ.get('VCAP_SERVICES'))
    nlc_user = str(services['natural_language_classifier'][0]['credentials']['username'])
    nlc_pwd = str(services['natural_language_classifier'][0]['credentials']['password'])
    classifierId=str(services['natural_language_classifier'][0]['credentials']['classifierId'])
else:
    f=open('../data/nlc-credentials.json','r')
    credentials=json.load(f)
    nlc_user=credentials['credentials']['username']
    nlc_pwd=credentials['credentials']['password']
    classifierId=credentials['credentials']['classifierId']
    f.close()
    print(classifierId)
    

# Setup Flask
port = int(os.getenv('VCAP_APP_PORT', 8080))
app = Flask(__name__, static_url_path='')

# setup dependant component
nlc= NLClassifier(nlc_user,nlc_pwd,classifierId)


@app.route("/")
def root():
  return app.send_static_file('home.html')


def buildAssessment():
	assessment={'status':'NEW'}
	d=date.today()
	assessment['creationDate']=d.strftime("%d/%m/%y")
	return assessment
	
# Process the first user query: The payload is a user query json object
@app.route("/dd/api/a/classify",methods=['POST'])
def classifyFirstUserQuery():
	userQuery=request.get_json()
	aQuery=userQuery['firstQueryContent']
	categories=nlc.classify(aQuery)
	userQuery['acceptedCategory']=categories['top_class']
	assessment=buildAssessment()
	assessment['customerQuery']=userQuery
	if (userQuery['acceptedCategory'] == 'battery'):
		aOut=BatteryProcessing.execute(assessment)
	else:
		#TODO add logic for processing other classes
		aOut=assessment
	print(aOut)
	return jsonify(aOut);

@app.route("/dd/api/a",methods=['POST'])
def dialog():
 	print(request.get_json())
 	pass
 	   	
if __name__ == "__main__":
	print("Server v0.0.1")
	app.run(host='0.0.0.0', port=port)
