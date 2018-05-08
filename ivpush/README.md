**版本控制流程**
1. spring事务管理
通过以上的问题我们可以发现。在spring中，在事务方法中调用多个事务方法时，spring将会把这些事务合二为一。当整个方法中每个子方法没报错时，整个方法执行完才提交事务（大家可以使用debug测试），如果某个子方法有异常，spring将该事务标志为rollback only。如果这个子方法没有将异常往上整个方法抛出或整个方法未往上抛出，那么该异常就不会触发事务进行回滚，事务就会在整个方法执行完后就会提交，这时就会造成Transaction rolled back because it has been marked as rollback-only的异常，就如上面代码中未抛throw e 一样。如果我们往上抛了改异常，spring就会获取异常，并执行回滚。
2. xxx