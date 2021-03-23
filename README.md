
# Android Demo

## branches
  - 1.`issue-`              issue Demo
  - 2.`use-`                第三方类库使用、语言用法
  - 3.`sdk-`                第三方SDK使用
  - 4.`framework`           app项目架构
  - 5.`code-`               源码分析

## app framework
- ### RecyclerView配套组件
    - [BaseRecyclerViewAdapter](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/adapter/BaseRecyclerViewAdapter.java)
    实现了上拉加载更多、空数据显示等。
    - [GridItemDivider](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/adapter/item_decoration/GridItemDivider.java)
	实现了GridLayoutManager边距设置。
	- [HorizontalItemDivider](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/adapter/item_decoration/HorizontalItemDivider.java)
	实现了LinearLayoutManager 水平方向的边距设置。
	- [VerticalItemDivider](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/adapter/item_decoration/VerticalItemDivider.java)
	实现了LinearLayoutManager 垂直方向的边距设置。
	- [AutoFixWidthLayoutManager](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/adapter/layout_manager/AutoFixWidthLayoutManager.java)
	实现了自适应宽度的控件进行按行排序布局。
- ### 选择器
	- [SelectionManage](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/utils/SelectionManage.java)
	实现了选择器抽象的数据模型。
	- [ThreeLayerSelectView](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/view/select/ThreeLayerSelectView.java)
	实现了三级菜单选择器。
	- [ListSelectView](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/view/select/ListSelectView.java)
	实现了列表选择器。
	- [LeftRightRecyclerView](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/view/LeftRightRecyclerView.java)
	实现了左右联动选择器。
- ### View
	- [SplashView](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/view/SplashView.java)
	实现了欢迎页封装。
	- [BottomTabView](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/view/BottomTabView.java)
	实现了底部导航栏Tab切换的封装。
	- [PageStatusView](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/view/PageStatusView.java)
	实现了页面View不同状态的显示。
- ### TabLayout
	- [AbsTabLayoutHelper](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/utils/tab/AbsTabLayoutHelper.java)
	实现任意样式Tab的选中和非选中状态的抽象封装。
	- [StringTabLayoutHelper](https://github.com/obelieve/Demo/blob/framework/app/src/main/java/com/zxy/demo/utils/StringTabLayoutHelper.java)
	实现String数据类型，样式Tab的选中和非选中状态的抽象封装例子。
- ### 弹窗
	- [PopupMenuUtil](https://github.com/obelieve/Demo/blob/framework/frame/src/main/java/com/zxy/frame/utils/PopupMenuUtil.java)
	实现了PopupMenu简易封装。
