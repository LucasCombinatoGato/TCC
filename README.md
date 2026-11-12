```mermaid
classDiagram
    class Usuário {
        +int cpf (PK)
        +String nome
        +String email
        +String senha
        +int cep_Local (FK)
        +String preferencia
    }

    class Produtores {
        +int cnpj (PK)
        +String nomeEmpresa
        +String email
        +String senha
        +int cep_Local (FK)
    }

    class Eventos {
        +int id (PK)
        +String nome
        +dateTime dataInicio
        +dateTime dataFim
        +String descricao
        +String tematica
        +String classificacaIndicativa
        +boolean estacionamento
        +int valorEstacionamento
        +int cepLocal (FK)
        +int id_InformacoesDias (FK)
        +int id_condicaoCompra (FK)
    }

    class Ong {
        +int cnpj (PK)
        +String nome
        +String descricao
        +int cepLocal (FK)
        +int id_InformacoesDias (FK)
    }

    class Local {
        +int cep (FK)
        +String tipoLogradouro
        +String logradouro
        +String bairro
        +String cidade
        +String uf
        +String numero
        +String complemento
    }

    class InformacoesDias {
        +int id (PK)
        +dateTime horaInicio
        +dateTime horaIntervalo
        +dateTime horaFinal
        +int quantIngressosTipo
    }

    class CondicaoCompra {
        +int id (PK)
        +String tipoEntrada
        +String tipoIngresso
        +String descricao
    }

    class DadosSistema {
        +int id
        +List<int> telefones
        +List<String> email
        +List<String> redesSociais
    }
    
    Produtores "1" *-- "many" Eventos : faz
    Ong "1" *-- "many" Eventos : faz
    Usuário "1" *-- "1" Local : pertence a
    Ong "1" *-- "1" Local : pertence a
    Produtores "1" *-- "1" Local : pertence a
    Local "1" *-- "many" Evento : contém

```
// 


UserProfile "1" *-- "many" CycleLog : manages
    EmergencyController "1" o-- "1" ChatSession : controls
    EmergencyController "1" o-- "1" MapService : uses

//


```mermaid
classDiagram
    class UserProfile {
      +String uid
      +bool isBiometricEnabled
      +authenticate() bool
      +triggerCamouflageMode() void
    }

    class CycleLog {
      +DateTime date
      +String mood
      +List~String~ symptoms
      +String flowLevel
      +saveToLocal() void
      +syncToCloud() void
    }

    class EmergencyController {
      +bool isHiddenModeActive
      +Location currentLocation
      +activateQuickEscape() void
      +fetchSafeRoutes() List~Route~
    }

    class ChatSession {
      +String sessionId
      +List~Message~ messages
      +sendMessage(String text)
      +sendQuickReply(QuickReplyType type)
    }

    class MapService {
      +getOSMTiles() MapData
      +calculateRoute(GeoPoint origin, GeoPoint dest) Route
    }

    UserProfile "1" *-- "many" CycleLog : manages
    EmergencyController "1" o-- "1" ChatSession : controls
    EmergencyController "1" o-- "1" MapService : uses

```

Usuário 
email (login) 
nome 
senha (login)
endereço-> cep, logradouro, cidade… 
preferências -> geek, musical, artesanato, cultural…

Produtores
email_empresa (login)
nome_empresa 
senha (login)
local_empresa 
CNPJ

Eventos
local -> CEP, edifício, complemento
nome
datas (de que dia até qual)
informações por dia:
id
hora início
hora final
quantidade ingressos disponível por tipo de ingresso
condições de compra (modal)
id
tipos de ingresso (vip, meia entrada, preferencial, etc)
valor/requisitos por tipo de ingresso
benefícios da cada tipo
descrição
temática
classificação indicativa
dados da empresa (comunicação, redes sociais)
estacionamento
valor do estacionamento
