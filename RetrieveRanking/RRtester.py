'''
Send document to retrieve and ranking services
Jerome Boyer - IBM  11/05/16
'''
import json
import requests

parameters={}

# Load parameters from json file
def loadRRparameters():
    f=open('./data/rr-parameters.json','r')
    doc=json.load(f)
    return doc['parameters']


def performGetRequest(url,user,pwd):
    ''' Perform the HTTP get request and return json response. This
    is a generic method
    '''
    try:
        r=requests.get(url,auth=(user,pwd))
        if r.status_code == 200:
            return json.loads(r.text)
    except  requests.exceptions.RequestException as e:
        print("Error"+str(e))
        return {'error':str(e)}

def getSolrCluster(baseUrl,user,pwd):
    '''
    From the clusters defined for the user this function return the first one.
    So this mean we assume one cluster
    {'clusters': [
    {'solr_cluster_id': 'scf33d875a_xxxxxx',
    'solr_cluster_status': 'READY', 'cluster_name': '', 'cluster_size': ''}
   ]}
    '''
    a=performGetRequest(baseUrl+"/v1/solr_clusters",user,pwd)
    return a['clusters'][0]

        
def getConfigId(baseUrl,user,pwd,clusterId):
    a=performGetRequest(baseUrl+"/v1/solr_clusters/"+clusterId+"/config",user,pwd)
    return a['solr_configs'][0]

def getRankerList(baseUrl,user,pwd):
    a=performGetRequest(baseUrl+"/v1/rankers",user,pwd)
    return a['rankers']

def getRanker(baseUrl,user,pwd,rankerId):
    a=performGetRequest(baseUrl+"/v1/rankers/"+rankerId,user,pwd)
    return a

def performRankedSearch(baseUrl,user,pwd,clusterId,colName,rankerId,query):
    rankerURL=baseUrl+"/v1/solr_clusters/%s/solr/%s/fcselect?ranker_id=%s" % (clusterId,colName,rankerId)
    a=performGetRequest(rankerURL
                        +'&q='+query
                        +'&wt=json&fl=id,title',
                        user,pwd)
    return a


if __name__ == "__main__":
    print('############## Test Retrieve and Ranking #######')
    print('## Load parameters from data/rr-parameters.json...');
    parameters=loadRRparameters()
    print('## Done..')
    print('\n## Get cluster information asigned to the user')
    cluster=getSolrCluster(parameters['url'],parameters['username'],parameters['password'])
    print('\n## Cluster Info: '+str(cluster))
    print('\n## Load configuration information')
    configId=getConfigId(parameters['url'],parameters['username'],parameters['password'],cluster['solr_cluster_id'],)
    print('\n## ConfigId:'+str(configId))
    print('\n## Load the list of rankers for the user')
    rankers=getRankerList(parameters['url'],parameters['username'],parameters['password'])
    for r in rankers:
        print(r)

    r=getRanker(parameters['url'],parameters['username'],parameters['password'],parameters['rankerId'])
    print('\n## Get Ranker information')
    print(r)
    if r['status'] != 'Training':
        query= 'Does battery die?'
        print('\n\n## Perform a search and ranking q='+query)
        # Perform a search using the trained R&R on battery recommendation
        results=performRankedSearch(parameters['url'],
                                parameters['username'],
                                parameters['password'],
                                parameters['clusterId'],
                                parameters['collectionName'],
                                parameters['rankerId'],
                                query)
        print(str(results))
                                
