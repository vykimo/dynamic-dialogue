import os, json
from flask import Flask

# Import Watson Service SDKs
from watson_developer_cloud import NaturalLanguageClassifierV1


# When deploying to Bluemix Watson Service Credentials of bound services are available in VCAP_SERVICES
if os.environ.get('VCAP_SERVICES'):
  services = json.loads(os.environ.get('VCAP_SERVICES'))
  nlc_user = str(services['natural_language_classifier'][0]['credentials']['username'])
  nlc_pass = str(services['natural_language_classifier'][0]['credentials']['password'])

# Setup Watson SDK
natural_language_classifier = NaturalLanguageClassifierV1(
    username=nlc_user,
    password=nlc_pass)


# Setup Flask
port = int(os.getenv('VCAP_APP_PORT', 8080))
app = Flask(__name__, static_url_path='')


@app.route("/")
def root():
  return app.send_static_file('home.html')
  
app.run(host='0.0.0.0', port=port)
