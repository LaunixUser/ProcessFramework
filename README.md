# ProcessFramework
This Java library supports the definition of process steps which can be invoked as a process chain

# Basics

The basic interface is `IProcessStep`:

![image](https://user-images.githubusercontent.com/5048297/54620245-2d619400-4a66-11e9-9752-4f53d8c775bf.png)

It encapsulates the key functionality of a process step:

* Accept input (source data S)
* Convert the input data to output data (T)
* Send the output data off to any number of subsequent processing steps
* Convert the output data to some (potentially other) form of output data U
* Send the data U off to output handlers for finalizing processing (these are not part of the processing chain but rather output steps along the way / during the processing chain) 

So, in a nutshell: instances of this interface take one kind of source data `<S>` and create one kind of target data `<T>`. They can also create data `<U>` for targets `(0 .. n)` which are invoked as part of the processing chain. 

Additionally, `IFilter<S>` instances can be added to the process step to filter the input data `<S>`.

# Processing Chain

The processing chain looks like this:

```
public void process(S data) {
  if (data == null) {
    throw new NullPointerException("data may not be null");
  }
  //.... Run the key process steps
  S filteredData = invokeFilters(data);
  T convertedData = convert(filteredData);
  U targetData = createTargetData(convertedData);
  invokeTargets(targetData);
  result = convertedData;
  if (this.nextStep != null) {
   this.nextStep.process(result);
 }
}
```

So first the input data is filtered, then the output data is created, then the target data is created, and then the targets are invoked. Last step is that this method invokes the next step in the process chain (if there is one specificed)

This method is implemented in the `AbstractProcessStep` class.

# Special Process Steps

## AbstractProcessStep

* Implements some of the interface methods and capacity to store e. g. targets and filters
* Provides implementations for `invokeFilters()` and `invokeTargets()`

## AbstractDirectProcessStep

* Extends `AbstractProcessStep`
* Covers the case that `<S> == <T>,` i. e. the data is just transferred
* Still targets are invoked and there can be filters applied
* Effectively, `convert()` is implemented as a no-op
* Leaves `createTargetData()` to be implemented

## AbstractTransferProcessStep

* Extends `AbstractProcessStep`
* Covers the case that `<U> == <T>`, i. e. the targets consume the output data directly without additional conversions
* Effectively, `createTargetData()` is implemented as a no-op
* Leaves `convert()` to be implemented

## SimpleProcessStep

* Extends `AbstractDirectProcessStep` (technically, could also implement `AbstractTransferProcessStep` but Java does not support multiple inheritance)
* Covers the case that `<S> == <U> == <T>`, i. e. the data is transferred and the targets consume the same data type
* Still targets are invoked and there can be filters applied
* Effectively, both `convert()` and `createTargetData()` are implemented as a no-op
* Useful if data needs to be fed to targets without actually touching it - except for filtering and/or sending to outputs




