# Todolist

Project make with Java, Spring Boot and H2 from a Todolist, follow link and avaliable routes:

### https://todolist-15p0.onrender.com

- User (<i>route</i> "/users"):
  - <b>Create</b> (post):
  ~~~json
        { 
          "name": "{name}", 
          "username": "{username_unique}", 
          "password": "{password}"
        }
  ~~~
- Task (<i>route</i> "/tasks" && Basic Authorization with username and password):
  - <b>Criar</b> (post):
  ~~~json
  {
    "title": "{title}",
    "description": "{description}",
    "startAt": "{startAt",
    "endAt": "{endAt}",
    "priority": "{priority}"
  }
  ~~~
  - <b>List by userId</b> (get)
  - <b>Update</b> (put): any or all fields of the create task
