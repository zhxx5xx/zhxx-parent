package com.zhxx.cart.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zhxx.cart.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.cart.pojo.Cart;
import com.zhxx.cart.service.CartService;
import com.zhxx.commons.pojo.Result;
import com.zhxx.commons.pojo.TbItemChild;
import com.zhxx.commons.utils.CookieUtils;
import com.zhxx.commons.utils.JsonUtils;
import com.zhxx.pojo.TbItem;
import com.zhxx.pojo.TbUser;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisPoolDao jedisPoolDaoImpl;

	@Value("${redis.user.key}")
	private String user;

	@Value("${redis.cart.key}")
	private String cart;

	@Value("${redis.item.key}")
	private String item;

	@Override
	public void addCart(long itemId, int num, HttpServletRequest request) throws Exception {
		// 根据商品ID从缓存中拿到item对象,并且得是带图片数组的对象
		/*
		 * 商品ID是从请求路径里传过来的，并且该路径是从商品详情页的请求域中取得的，而在查询商品详情的时候已经在redis创建过该商品的缓存，
		 * 所以一定可以从redis中取得该商品对象
		 */
		String itemJson = jedisPoolDaoImpl.get(item + itemId);
		TbItemChild itemChild = JsonUtils.jsonToPojo(itemJson, TbItemChild.class);

		// 能走到这里说明一定已经登录过了，所以可以直接去redis里拿到那个TbUser对象
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String userJson = jedisPoolDaoImpl.get(user + uuid);
		TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);

		String cartKey = cart + user.getId();

		if (jedisPoolDaoImpl.exists(cartKey)) {
			// 这个用户在缓存中创建过购物车
			String cartJason = jedisPoolDaoImpl.get(cartKey);
			List<Cart> list = JsonUtils.jsonToList(cartJason, Cart.class);

			// 开始判断这个用户在购物车中有没有添加过这个商品
			int index = -1;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId() == itemId) {
					index = i;
					break;
				}
			}

			if (index == -1) {
				// 这个用户在购物车中并没有添加过这个商品
				Cart cart = new Cart(itemChild.getId(), itemChild.getPrice(), itemChild.getTitle(), num,
						itemChild.getImages());
				list.add(cart);
			} else {
				// 这个用户在购物车中并添加过这个商品
				Cart cart = list.get(index);
				cart.setNum(cart.getNum() + num);
			}
			jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));

		} else {
			// 这个用户并没有在缓存中创建过购物车
			List<Cart> list = new ArrayList<>();
			Cart cart = new Cart(itemChild.getId(), itemChild.getPrice(), itemChild.getTitle(), num,
					itemChild.getImages());
			list.add(cart);
			jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));
		}
	}

	@Override
	public List<Cart> showCart(HttpServletRequest request) {
		// 从redis中获取已经添加的list
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String userJson = jedisPoolDaoImpl.get(user + uuid);
		TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);

		String cartKey = cart + user.getId();
		String cartJason = jedisPoolDaoImpl.get(cartKey);
		List<Cart> list = JsonUtils.jsonToList(cartJason, Cart.class);

		return list;
	}

	@Override
	public Result upItemNum(long itemId, int num, HttpServletRequest request) {
		Result rs = new Result();

		// 从redis中获取已经添加的list
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String userJson = jedisPoolDaoImpl.get(user + uuid);
		TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);

		String cartKey = cart + user.getId();

		String cartJason = jedisPoolDaoImpl.get(cartKey);
		List<Cart> list = JsonUtils.jsonToList(cartJason, Cart.class);

		// 修改list中的那个商品的数量
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == itemId) {
				list.get(i).setNum(num);
				break;
			}
		}

		// 修改数量后将最新的数量放到redis中
		String result = jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));

		// 判断redis是否修改成功
		if ("OK".equalsIgnoreCase(result)) {
			rs.setStatus(200);
		} else {
			rs.setStatus(400);
		}
		return rs;
	}

	@Override
	public Result del(long itemId, HttpServletRequest request) {
		Result rs = new Result();

		// 从redis中获取已经添加的list
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String userJson = jedisPoolDaoImpl.get(user + uuid);
		TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);

		String cartKey = cart + user.getId();

		String cartJason = jedisPoolDaoImpl.get(cartKey);
		List<Cart> list = JsonUtils.jsonToList(cartJason, Cart.class);

		Iterator<Cart> iterator = list.iterator();

		while (iterator.hasNext()) {
			if (iterator.next().getId() == itemId) {
				iterator.remove();
				break;
			}
		}

		// 删除后将最新的list放到redis中
		String result = jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));

		// 判断redis是否修改成功
		if ("OK".equalsIgnoreCase(result)) {
			rs.setStatus(200);
		} else {
			rs.setStatus(400);
		}

		return rs;
	}

	@Override
	public List<Cart> showCartOrder(HttpServletRequest request, List<Long> ids) {
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String userJson = jedisPoolDaoImpl.get(user+uuid);
		TbUser tbUser = JsonUtils.jsonToPojo(userJson, TbUser.class);
		String cartKey = cart+tbUser.getId();
		List<Cart> list = JsonUtils.jsonToList(jedisPoolDaoImpl.get(cartKey), Cart.class);
		
		boolean enough = true;
		List<Cart> listCartOrder = new ArrayList<>();
		for (long id : ids) {
			for (Cart cart : list) {
				if(cart.getId()==id) {
					listCartOrder.add(cart);
					TbItemChild child = JsonUtils.jsonToPojo(jedisPoolDaoImpl.get(item+id), TbItemChild.class);
					if(child.getNum()<cart.getNum()) {
						enough = false;
					}
					break;
				}
			}
		}
		
		if(enough) {
			return list;
		}
		
		return null;
	}

	
}
