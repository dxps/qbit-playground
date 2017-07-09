# QBit Playground

This playground includes various QBit examples, some of them copied as-is from [QBit Wiki], while others include
some changes to better illustrate the capabilities of QBit library and the use cases.

### QBit > Samples > TODO Service versions

These samples shows how to implement a todo service
using QBit with callbacks and service pools.

##### Run

Start `qbit.samples.todo.callback.TodoServiceMain` to run the version using callbacks.

Start `qbit.samples.todo.workers.TodoServiceWorkersMain` to run the version using callbacks .

##### Usage

- List existing todos:

  ```bash
  curl http://localhost:8080/services/todo-service/todo/
  ```
  
- Add a new todo:

  ```bash
  curl -X POST -H "Content-Type: application/json" \
  -d '{"name":"First Task","description":"Something to do ..."}' \
  http://localhost:8080/services/todo-service/todo
  ```
  
- Count the number of todos:

  ```bash
  curl http://localhost:8080/services/todo-service/todo/count
  ```

##### Testing the responsiveness:

```bash
time curl "http://localhost:8080/v1/todo-service/todo/exec-op-sync?execTime=4" &
time curl "http://localhost:8080/v1/todo-service/todo" &
   
```

```bash
time curl "http://localhost:8080/v1/todo-service/todo/exec-op-async?execTime=6" &
time curl "http://localhost:8080/v1/todo-service/todo/exec-op-async?execTime=2" &
time curl "http://localhost:8080/v1/todo-service/todo" &
   
```


### QBit Reactor > Samples > HR

##### Run

Start `qbit.samples.hr.HRServiceMain`.

##### Usage

- List departments:

  ```bash
  
  time curl http://localhost:8888/v1/hr/department/
  
  ```

- Add departments:

  ```bash
    
  time curl -X POST -H "Content-Type: application/json" \
            -d '{ "id":1, "name":"Dept 1" }' \
            http://localhost:8888/v1/hr/department/1 &
  
  time curl -X POST -H "Content-Type: application/json" \
            -d '{ "id":2, "name":"Dept 2" }' \
            http://localhost:8888/v1/hr/department/2 &
     
  ```
  
#### Testing the responsiveness

- Calling the following operations, the second one should quickly respond and 
  it should not be blocked by the first one (who takes 5 seconds to complete):
  
  ```bash
  
  time curl -X POST -H "Content-Type: application/json" \
              -d '{ "id":3, "name":"Dept 3" }' \
              http://localhost:8888/v1/hr/department/2 &
              
  time curl http://localhost:8888/v1/hr/department/ &
     
  ```

