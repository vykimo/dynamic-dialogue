'''

Created on Jun 2, 2016

@author: Jerome Boyer - IBM
'''

import json 
from os.path import join, dirname

from watson_developer_cloud import DocumentConversionV1


document_conversion = DocumentConversionV1(
    username='d75bba69-6615-4c2a-9b93-8481e9e7b6f8',
    password='uBULgr5MfvxI',
    version='2016-02-09')

def convertBattryDocToAnswerUnit():
    with open(('./data/Battery.docx'), 'rb') as document:
        config = {'conversion_target': DocumentConversionV1.ANSWER_UNITS}
        return document_conversion.convert_document(document=document, config=config)
    
if __name__ == '__main__':
    #print(json.dumps(buildAnswerUnits(), indent=2))
    print(json.dumps(convertBattryDocToAnswerUnit(), indent=2))
    # print(buildNormalizedHtml())
