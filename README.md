# 异步任务Promise

这个框架用于Java的异步任务，在Java中，异步任务需要使用线程```Thread```这个概念去完成，在一个持久运行的项目中，频繁创建和开启一个全新的线程会消耗不必要的资源，同时Java官方提出了线程池```ThreadPool``` 这个概念，来维护和缓存一些线程，在有任务的时候将这些已经创建好的线程重新激活并复用，这样可以避免频繁创建开启新的线程。

这个框架内置了一套线程池，并且提供了任务这个概念，在概念上任务将小于线程单位，内部仍然使用线程池去实现，可以让开发者不关心线程的创建和线程池的创建，而将精力全部放置于业务中。

### 1、版本要求

``` text
Java 17
```

### 2、引用
```xml
<dependency>
  <groupId>com.yuqianhao</groupId>
  <artifactId>PromiseTask</artifactId>
  <version>1.1.9</version>
</dependency>
```

``` xml
<dependency>
  <groupId>com.yuqianhao</groupId>
  <artifactId>PromiseTask</artifactId>
  <version>1.1.7</version>
</dependency>
```

### 3、初始化线程池

这个步骤可以省略，如果开发者想要精确地控制线程池的线程数量，就需要在程序刚开始执行的时候，调用这个方法来配置线程池。

```java
Promise.config(int corePoolSize,int maximumPoolSize);
```

| 参数            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| corePoolSize    | 线程池的核心数量。线程池在没有任务时时会保留这些数量的线程，并暂停等待，直到有新的任务加入，才会重新被激活。 |
| maximumPoolSize | 线程池中允许的最大线程数。当核心线程全部处于工作状态，但是有新的任务加入，如果核心线程数量小于最大线程数量，线程池会创建一个全新的线程来执行这个新的任务，当这个新的任务执行结束时，线程池会立即关闭并释放这个线程，继续维持核心线程数量。 |

当然，这个工作开发者可以忽略，Promise中预设的核心线程数量是```核心处理器数量*2```，最大线程数量是```核心处理器数量*4```。

### 4、提交一个异步任务，无需获取返回值

Promise提供了一个名为```async```的方法，这个方法有3个重载，其中一个是用来提交异步任务并且无需等待结果的方法。

```java
void async(Runnable runnable);
```

```java
Promise.async(()->{
	System.out.println("Hello World!");
});
```

这个方法接受一个```Runnable```对象的实例，并且会将这个实例提交到线程池等待任务被执行。

### 5、提交一个异步任务，需要获取返回值

Promise提供的另外两个```async```方法的重载都可以获取返回值，其中一个方法是采用回调的方式获取返回值，另外一个方法使用直接返回的方式来获取返回值。

#### （1）、直接获取返回值

```Promise.async()```方法接受一个```IPromiseTask<ResultType>```为参数类型的实例，这个类是一个接口类，接口定义如下

```java
public interface IPromiseTask<ResponseType> {
    ResponseType run() ;
}
```

这个接口类接受一个```ResponseType```为执行结果的类型，通常开发者不需要明确指出，只需要将返回结果返回即可。

```java
Result<ResultType> async(IPromiseTask<ResultType> task);
```

```java
Result<String> nameResult=Promise.async(()->{
	String name="Tim";
	return "This is "+name;
});
String name=nameResult.get();
```

这个方法会在任务被提交时，结果立即返回，但是这个结果并不是真正的执行结果，而是被```Result```持有的任务结果，```Result```提供了一个```get```方法，这个方法可以获取到任务执行结果，但是需要等待任务执行结束，意味着如果任务没有执行结束，而调用了```get```方法，调用线程将会被阻塞，直到任务完成才会继续执行。

```Result```还提供了其他的方法用来判断任务是否执行结束，这两个方法调用会立即返回，不会阻塞调用线程。

```java
nameResult.isDone();
nameResult.isRunning();
```

| 方法名    | 说明                 | 返回值                                  |
| --------- | -------------------- | --------------------------------------- |
| isDone    | 判断任务是否执行完成 | 返回一个boolean值，true表示已经执行完成 |
| isRunning | 判断任务是否还在运行 | 返回一个boolean值，true表示还在执行     |

#### （2）、通过回调的方式获取返回值

```Promise.async()```方法还有一个重载方法用来回调返回结果，这个重载的方法接受```AbsAsyncResult<ResultType>```和```IPromiseTask<ResultType>```的参数实例，```AbsAsyncResult<ResultType>```作为回调结果的数据类型，实现了这个抽象类的实例需要实现```void onCall(ResultType result)```方法，当任务执行结束时，线程池会通过这个方法将结果进行回调。

```java
void async(IPromiseTask<ResultType> task, IAsyncResult<ResultType> callback);
```

```java
Promise.async(()->{
	String name="Tim";
	return "This is "+name;
},result -> {
	System.out.println(result);
});
```

### 6、提交多个异步任务并同时获取所有返回结果

```Promise```提供了一个方法```all```来支持同时提交多个异步任务，并按照提交的顺序同时获取到所有执行结果。

```java
PromiseArrayResult all(IRunnableTask... task);
```

这个方法接受多个```IRunnableTask```执行单元的实例，这个执行单元是一个接口类，并且只包含一个```run```方法。

```java
public interface IRunnableTask {
    Object run();
}
```

这个方法的返回结果由类```PromiseArrayResult```提供支持，它提供了一个```get```方法来获取一个```Object[]```，这个```Object[]```代表了这个任务组中所有任务的返回值，数组中的下标会遵从任务添加的顺序。

```java
PromiseArrayResult result=Promise.all(()->{
	RuntimeVM.sleepCurrentThread(3000);
	return "A";
},()->{
	RuntimeVM.sleepCurrentThread(1000);
	return "B";
},()->{
	RuntimeVM.sleepCurrentThread(2000);
	return "C";
});
System.out.println(Arrays.toString(result.get()));
```

```text
[控制台输出]
A
B
C
```

```PromiseArrayResult```的```get```方法是一个阻塞方法，这个返回在被调用时会等待所有的任务全部执行结束后才会返回，如果在调用时有任务没有执行完毕，会阻塞当前线程。

```PromiseArrayResult```还提供了一组方法，用来检查任务组的状态。

```java
PromiseArrayResult.isRunning();
PromiseArrayResult.isDone();
PromiseArrayResult.getCompleteCount();
PromiseArrayResult.getTaskCount();
```

| 方法             | 说明                       | 返回值                                                |
| ---------------- | -------------------------- | ----------------------------------------------------- |
| isRunning        | 任务组是否还在继续执行     | 返回一个boolean类型，值为true时代表任务组还在执行     |
| isDone           | 任务组是否执行结束         | 返回一个boolean类型，值为true时代表任务组已经执行结束 |
| getCompleteCount | 任务组已经执行完的任务数量 | 返回一个int类型                                       |
| getTaskCount     | 任务组中全部任务的数量     | 返回一个int类型                                       |

### 到这里，```Promise```已经全部介绍完毕。
