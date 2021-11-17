import requests

# Dicionário que armazena a URL e a requisição à URL, funcionando como cache
dicionarioRequests = {}

# Checa se a URL já foi requisitada anteriormente
def checaCache(url):
    global dicionarioRequests

    # Se ainda não foi requisitada, faz a chamada à API
    if url not in dicionarioRequests:
        response = requests.get(url)
        if response.status_code == 200:
            response = response.json()
            dicionarioRequests[url] = response
        elif response.status_code == 404:
            print("Usuário não encontrado")
            response = None
        elif response.status_code == 403:
            print("Limite de requisições atingido")
            response = None
            
    else:
        # Pega a resposta armazenada no cache
        response = dicionarioRequests[url]
    return response

while True:
    # Recebe o nome do usuário
    user = input("Informe o usuário que deseja buscar: ")

    if user:
        url = "https://api.github.com/users/" + user + "/followers"
        response = checaCache(url)
        
        # Caso a requisição seja bem sucedida, informa o número de seguidores, lista cada um
        # imprimindo o nome completo e o nome de seus repositórios.
        if response is not None:
            print(f"\nUsuário da consulta: {user}\n")
            print(f"{len(response)} seguidores:\n")
            for i in response:
                url = "https://api.github.com/users/" + i["login"]
                responseFollower = checaCache(url)
                if(responseFollower is not None):
                    print(responseFollower["name"])
                    url = "https://api.github.com/users/" + i["login"] + "/repos"
                    responseRepos = checaCache(url)
                    if(responseRepos is not None):
                        for j in responseRepos:
                            print(f"\t{j['name']}")
                    else:
                        break
                else:
                   break
        # else:
        #     break
            
    else:
        print("Usuário inválido!")

    opcao = input("Continuar pesquisando? 1-Sim\t2-Não\nOpção: ")
    if(opcao != '1'):
        break
