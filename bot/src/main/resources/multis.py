#Para entrar a los mails, ir a http://www.fakemailgenerator.com/#/armyspy.com/<user>/
import requests
endpoint = "https://www.quiendamenos.com"
headers = {"Content-Type":"application/x-www-form-urlencoded", "Referer":"https://www.quiendamenos.com/?afid=47499"}
users = ["Aculd1941","Peacher82","Makined64","Theapoes","Ninsat92","Obace1983","Lech1940","Lowas1938","Hurew1956","Lierearmeng","Prome1932"]
for user in users:
    body = {"username":user, "password":"123456", "email":user+"@armyspy.com", "citi":"neuquen", "afiliaor":47499}
    r = requests.post(url,body,headers=headers)
    print(r)