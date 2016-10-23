import os, json, requests
from flask import Flask,request,jsonify
from datetime import date

# Import Watson Service SDKs
from watson_developer_cloud import NaturalLanguageClassifierV1 as NLC


# When deploying to Bluemix Watson Service Credentials of bound services are available in VCAP_SERVICES
if os.environ.get('VCAP_SERVICES'):
  services = json.loads(os.environ.get('VCAP_SERVICES'))
  nlc_user = str(services['natural_language_classifier'][0]['credentials']['username'])
  nlc_pwd = str(services['natural_language_classifier'][0]['credentials']['password'])
else:
    f=open('./nlc-credentials.json','r')
    credentials=json.load(f)
    nlc_user=credentials['credentials']['username']
    nlc_pwd=credentials['credentials']['password']
    url=credentials['credentials']['url']
    classifierId=credentials['credentials']['classifierId']
    
# Setup Watson SDK
natural_language_classifier = NLC(username=nlc_user,password=nlc_pwd)

# Setup Flask
port = int(os.getenv('VCAP_APP_PORT', 8080))
app = Flask(__name__, static_url_path='')


@app.route("/")
def root():
  return app.send_static_file('home.html')
  
@app.route("/dd/api/a/classify",methods=['POST'])
def callNLC():
	cq=request.get_json()
	aQuery=cq['firstQueryContent']
	assessment={'status':'NEW'}
	d=date.today()
	assessment['creationDate']=d.strftime("%d/%m/%y")
	assessment['customerQuery']=cq
	categories=classify(natural_language_classifier,classifierId,aQuery)
	cq['acceptedCategory']=categories['top_class']
	assessment['customerQuery']=cq
	print(assessment)
	return jsonify(assessment);

def classify(natural_language_classifier,cid,text):
    classes = natural_language_classifier.classify(cid, text)   
    return classes
    	
app.run(host='0.0.0.0', port=port)
