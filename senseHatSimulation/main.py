import time
import communication
from sense_emu import SenseHat

sense = SenseHat()

client = communication.connect_mqtt()
client.loop_start()

tData = communication.tData
pData = communication.pData
hData = communication.hData

while True:
    time.sleep(communication.frequence)
    """permet de changer la fréquence d'échantillonnage en arrêtant le programme le temps indiqué en paramètre"""
    temperature = round(sense.get_temperature(), 1)
    """collecte de données de température"""
    pression = round(sense.get_pressure(), 1)
    """collecte de données de pression"""
    humidite = round(sense.get_humidity(), 1)
    """collecte de données d'humidité"""
    communication.publish(client, temperature, pression, humidite, tData, pData, hData)
    """envoi des données collectées"""
