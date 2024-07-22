# Documentação da API de Tarefas

## Visão Geral

Esta é a documentação para a API de gerenciamento de tarefas. A API permite criar, ler, atualizar e excluir tarefas. Ela utiliza o formato JSON para comunicação e segue os padrões REST.

## Endpoints

### 1. Listar Tarefas

- **Método:** `GET`
- **URL:** `/api/tasks`
- **Descrição:** Retorna uma lista paginada de tarefas.
- **Parâmetros de Consulta:**
  - `page` (opcional): Número da página para a paginação (padrão: 0).
  - `size` (opcional): Número de itens por página (padrão: 10).
- **Resposta:**
  - **Código 200 OK:** Retorna um JSON com uma lista de tarefas.
  - **Exemplo de Resposta:**
    ```json
    {
      "content": [
        {
          "id": 1,
          "title": "Tarefa 1",
          "description": "Descrição da tarefa 1",
          "deadline": "2024-08-22",
          "completed": false,
          "isOverdue": false
        }
      ],
      "pageable": {
        "sort": { "sorted": false, "unsorted": true, "empty": true },
        "offset": 0,
        "pageSize": 10,
        "pageNumber": 0,
        "paged": true,
        "unpaged": false
      }
    }
    ```

### 2. Obter Tarefa por ID

- **Método:** `GET`
- **URL:** `/api/tasks/{id}`
- **Descrição:** Retorna uma tarefa específica pelo ID.
- **Parâmetros de URL:**
  - `id` (obrigatório): ID da tarefa.
- **Resposta:**
  - **Código 200 OK:** Retorna a tarefa encontrada.
  - **Código 404 Not Found:** Tarefa não encontrada.
  - **Exemplo de Resposta:**
    ```json
    {
      "id": 1,
      "title": "Tarefa 1",
      "description": "Descrição da tarefa 1",
      "deadline": "2024-08-22",
      "completed": false,
      "isOverdue": false
    }
    ```

### 3. Criar Tarefa

- **Método:** `POST`
- **URL:** `/api/tasks`
- **Descrição:** Cria uma nova tarefa.
- **Corpo da Requisição:**
  - **Exemplo:**
    ```json
    {
      "title": "Nova Tarefa",
      "description": "Descrição da nova tarefa",
      "deadline": "2024-09-01",
      "completed": false
    }
    ```
- **Resposta:**
  - **Código 201 Created:** Tarefa criada com sucesso.
  - **Exemplo de Resposta:**
    ```json
    {
      "message": "Task created successfully",
      "task": "Task{id=1, title='Nova Tarefa', description='Descrição da nova tarefa', deadline=2024-09-01, completed=false}"
    }
    ```

### 4. Atualizar Tarefa

- **Método:** `PUT`
- **URL:** `/api/tasks/{id}`
- **Descrição:** Atualiza uma tarefa existente pelo ID.
- **Parâmetros de URL:**
  - `id` (obrigatório): ID da tarefa a ser atualizada.
- **Corpo da Requisição:**
  - **Exemplo:**
    ```json
    {
      "title": "Tarefa Atualizada",
      "description": "Descrição atualizada",
      "deadline": "2024-09-15",
      "completed": true
    }
    ```
- **Resposta:**
  - **Código 200 OK:** Tarefa atualizada com sucesso.
  - **Código 404 Not Found:** Tarefa não encontrada.
  - **Exemplo de Resposta:**
    ```json
    {
      "message": "Task updated successfully",
      "task": "Task{id=1, title='Tarefa Atualizada', description='Descrição atualizada', deadline=2024-09-15, completed=true}"
    }
    ```

### 5. Atualizar Status de Conclusão da Tarefa

- **Método:** `PATCH`
- **URL:** `/api/tasks/{id}/completed`
- **Descrição:** Atualiza o status de conclusão de uma tarefa.
- **Parâmetros de URL:**
  - `id` (obrigatório): ID da tarefa.
- **Parâmetros de Consulta:**
  - `completed` (obrigatório): Novo status de conclusão (`true` ou `false`).
- **Resposta:**
  - **Código 200 OK:** Status de conclusão atualizado com sucesso.
  - **Código 404 Not Found:** Tarefa não encontrada.
  - **Exemplo de Resposta:**
    ```json
    {
      "message": "Task completion status updated successfully"
    }
    ```

### 6. Deletar Tarefa

- **Método:** `DELETE`
- **URL:** `/api/tasks/{id}`
- **Descrição:** Remove uma tarefa pelo ID.
- **Parâmetros de URL:**
  - `id` (obrigatório): ID da tarefa a ser deletada.
- **Resposta:**
  - **Código 200 OK:** Tarefa deletada com sucesso.
  - **Código 404 Not Found:** Tarefa não encontrada.
  - **Exemplo de Resposta:**
    ```json
    {
      "message": "Task deleted successfully"
    }
    ```
