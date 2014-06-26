/**
 * 创建日期 2013-3-20
 * 创建用户 Administrator
 * 变更情况:
 * 文档位置 $Archive$
 * 最后变更 $Author$
 * 变更日期 $Date$
 * 当前版本 $Revision$
 * 
 * Copyright (c) 2004 Sino-Japanese Engineering Corp, Inc. All Rights Reserved.
 * 
 */
package com.jetsoft.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import com.jetsoft.entity.ProductEntity;

public class CountList extends LinkedList<ProductEntity> implements Serializable{
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(ProductEntity object) {
		for(ProductEntity entity : this){
			if(entity.getId().equals(object.getId()) && !object.isCopy()){
				if(object.getNum()>1){
					entity.setNum(entity.getNum()+object.getNum());
					entity.setNumUnit1(entity.getNumUnit1()+object.getNumUnit1());
				}else{
					entity.setNum(entity.getNum()+1);
					entity.setNumUnit1(entity.getNumUnit1()+1);
				}
				return true;
			}
		}
		return super.add(object);
	}
	
	/**
	 * 添加商品
	 * @param object 商品
	 * @param directly 是否直接添加，如果true则直接添加到列表，不进行id验证及数量相加，false则判断id，如果id相同则只改变数量
	 * @return
	 */
	public boolean add(ProductEntity object,boolean directly){
		if(directly){
			return super.add(object);
		}else{
			return add(object);
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends ProductEntity> collection) {
		boolean flag = true;
		for(ProductEntity entity : collection){
			flag = flag && this.add(entity);
		}
		return flag;
	}
}
