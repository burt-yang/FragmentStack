# FragmentStack
一个轻量级管理Android Fragment的栈结构 效果图如下

![img](https://github.com/burtYang/FragmentStack/blob/master/app/src/main/res/drawable/demo.gif)
 
    创建Fragment栈
    mFragmentStack = FragmentStack.create(getSupportFragmentManager(), R.id.fl_container);
    
    使用
    mFragmentStack.push(fragment, tag);
    mFragmentStack.replace(fragment, tag);
    mFragmentStack.pop();
    mFragmentStack.clear();
    
    
    欢迎star
