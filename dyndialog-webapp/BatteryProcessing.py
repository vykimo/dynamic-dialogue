'''
This module supports the logic to process a battery conversation
10/2016 Jerome Boyer - IBM
'''
from bmx_rs_client import RuleServiceClient
from crm_client import CRM
import json

ruleApp = {
  'name': 'DDRuleApp',
  'appArchive': 'data/ruleApp_DDRuleApp_1.0.jar',
  'appModel': 'data/dyndialog-model.zip'
}


rs=RuleServiceClient(ruleApp = ruleApp)

'''
Use first ruleset in ODM / IBM Business Rules to assess the data quality and as
potential outcome 
'''
def assessDataNeed(assessment):
        a=rs.assessDataNeed(assessment)
        end=False
        while not end:
                if a['status'] == 'Uncompleted':
                        crm=CRM()
                        if isRecommendationPresent(a,'Load Customer'):
                                customer=crm.loadCustomer(a['customerId'])
                                a['customerContext']=customer
                        if isRecommendationPresent(a,'Load Product'):
                                products=crm.loadProducts(a['customerId'])
                                a['customerContext']['ownedProducts']=products
                        a=rs.assessDataNeed(a)
                else:
                        if a['status'] == 'DataCompleted':
                                a=rs.processQuestion(a)
                        end=True
        return a

'''
   Main entry point to process the logic flow of the Battery Conversation
'''
def execute(assessment):
        # First assess what data to load
        if assessment['status'] == 'New':
                a=assessDataNeed(assessment)
        else:
                a=rs.processQuestion(assessment)
        return a


def addRecommendation(assessment,message):
        recommendations=assessment['recommendations']
        
               
def isRecommendationPresent(assessment,recommendation):
        recommendations=assessment['recommendations']
        for r in recommendations:
                if recommendation in r['message']:
                        return True
        return False


if __name__ == "__main__":
        a={"customerId":'bill',"status":"New","customerQuery":{"firstQueryContent":"battery","categories":[],"acceptedCategory":"battery"},"creationDate":"2016-10-28T00:00:00.000+0000"}
#       c=CRM.loadCustomer(a,'bill')
#       a['assessment']['customerContext']=c
        a=execute(a)
        print(a)
