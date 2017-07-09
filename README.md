# QBit Playground

This playground includes various QBit examples, some of them copied as-is from [QBit Wiki](https://github.com/advantageous/qbit/wiki), while others include
some changes to better illustrate the capabilities of QBit library and the use cases.

-----

### QBit > Samples > TODO Service versions

These samples shows how to implement a todo service using QBit with callbacks and service workers.

#### Run

Start `qbit.samples.todo.callback.TodoServiceMain` to run the version using callbacks.

Start `qbit.samples.todo.workers.TodoServiceWorkersMain` to run the version using callbacks and workers.

#### Usage

- List the todos:

  ```bash
  curl http://localhost:8080/v1/todo-service/todo
  ```
  
- Add a new todo:

  ```bash  
  curl -X POST -H "Content-Type: application/json" \
  -d '{"name":"First Task","description":"Something to do ..."}' \
  http://localhost:8080/v1/todo-service/todo
  ```
  
- Count the number of todos:

  ```bash
  curl http://localhost:8080/v1/todo-service/todo/count
  ```

#### Testing the responsiveness:

- Test 1 > Sending requests that are processes in a synchronous way
  ```bash
  time curl "http://localhost:8080/v1/todo-service/todo/exec-op-sync?execTime=4" &
  time curl "http://localhost:8080/v1/todo-service/todo" &
  ```
  Since `/exec-op-sync` request processing uses a sync call (without a callback), the second request (to `/todo` for listing the todos) is waiting on the queue to be served. Having this, the response time of `/todo` request is over 4 seconds, although it can be delivered in a 0.1 second.

- Test 2 > Sending requests that are processes in an asynchronous way
  ```bash
  time curl "http://localhost:8080/v1/todo-service/todo/exec-op-async?execTime=4" &
  time curl "http://localhost:8080/v1/todo-service/todo/exec-op-async?execTime=2" &
  time curl "http://localhost:8080/v1/todo-service/todo" & 
  ```
  In this case, `/exec-op-async` request processing is using callbacks. Still, the first two requests are processed by the same thread (part of ExecuteOp service queue), and thus are queued and delivered sequentially.
  But the third request is delivered right away, without any delay.

  That is because the callbacks used in `execOpAsync(...)` method (that processes `/todo/exec-op-async` requests)
does not block the thread of main (`TodoService`'s) service queue.

-----

### QBit Reactor > Samples > HR

#### Run

Start `qbit.samples.hr.HRServiceMain`.

#### Usage

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

- Test 1 > First request is async processed, thus it does not block the main service queue
  
  ```bash
  
  time curl -X POST -H "Content-Type: application/json" \
              -d '{ "id":3, "name":"Dept 3" }' \
              http://localhost:8888/v1/hr/department/2 &
              
  time curl http://localhost:8888/v1/hr/department/ &
  ```
  The second request should be processed quickly, as it is not blocked by the first request (that takes 5 sec to complete).

