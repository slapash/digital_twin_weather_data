import time
from configparser import ConfigParser
from paho.mqtt import client as mqtt_client
import json

config_object = ConfigParser()
config_object.read("config.ini")
serverconfig = config_object["SERVERCONFIG"]
datainfo = config_object["DATAINFO"]
broker = serverconfig["broker"]
port = int(serverconfig["port"])
topicT = datainfo["topict"]
topicP = datainfo["topicp"]
topicH = datainfo["topich"]
topicC = datainfo["topicc"]
frequence = float(datainfo["frequence"])
client_id = serverconfig["client_id"]
tData = int(datainfo["tData"])
pData = int(datainfo["pData"])
hData = int(datainfo["hData"])


def connect_mqtt():
    """se connecter à un broker mqtt

    Returns:
    object: client connected to broker

    """
    client = mqtt_client.Client(client_id)
    """attribue l'identifiant au client client"""
    client.connect(broker, port)
    """connecte le client au broker"""
    return client


def publish(client, t, p, h, tdata, pdata, hdata):
    """publier des données avec leur timestamps sur les topics en parametre

    Parameters:
    argument1 (object): client mqtt retourné dans la fonction connect_mqtt
    argument2 (string): topic à utiliser pour l'envoi de données
    (ici t représente la température)
    argument3 (string): topic à utiliser pour l'envoi de données
    (ici p représente la pression)
    argument4 (string): topic à utiliser pour l'envoi de données
    (ici h représente l'humidité')
    argument5 (int): valeur de 0 ou 1 pour choisir si l'on veut envoyer
    le premier argument ou non (température)
    argument6 (int): valeur de 0 ou 1 pour choisir si l'on veut envoyer
    le second argument (pression)
    argument7 (int): valeur de 0 ou 1 pour choisir si l'on veut envoyer
    le troisième argument (humidité)

    """
    date_collecte = time.time() * 1000

    if (tdata):
        msgT = json.dumps({'value': t, 'date': date_collecte})
        client.publish(topicT, msgT)
        print(f"Send `{msgT}` to topic `{topicT}`")

    if (pdata):
        msgH = json.dumps({'value': h, 'date': date_collecte})
        client.publish(topicH, msgH)
        print(f"Send `{msgH}` to topic `{topicH}`")

    if (hdata):
        msgP = json.dumps({'value': p, 'date': date_collecte})
        client.publish(topicP, msgP)
        print(f"Send `{msgP}` to topic `{topicP}`")

    msgC = json.dumps({'value': 1, 'date': date_collecte})

    client.publish(topicC, msgC)
    print(f"Send `{msgC}` to topic `{topicC}`")
