package com.jetsoft.activity.baobiao;
/**
 * 报表结果的list的ViewHolder
 * @author funever
 *
 */
public class ViewHolderFactory {
	/**
	 * 根据type获取ViewHolder
	 * @param type
	 * @return
	 */
	public static ViewHolder newViewHolder(int type){
		switch(type){
			case BaoBiaoActivity.XJYH:
				return new XJYHViewHolder();
			case BaoBiaoActivity.WLDW:
				return new WLViewHolder();
			case BaoBiaoActivity.KCZK:
				return new KCZKViewHolder();
			case BaoBiaoActivity.JHHZ:
				return new JHHZViewHolder();
			case BaoBiaoActivity.XSHZ:
				return new XSHZViewHolder();
			case BaoBiaoActivity.JYLC:
				return new JYLCViewHolder();
		}
		return new DefaultViewHolder();
	}
}
