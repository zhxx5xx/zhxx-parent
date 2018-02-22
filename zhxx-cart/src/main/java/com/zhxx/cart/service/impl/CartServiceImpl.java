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
		// ������ƷID�ӻ������õ�item����,���ҵ��Ǵ�ͼƬ����Ķ���
		/*
		 * ��ƷID�Ǵ�����·���ﴫ�����ģ����Ҹ�·���Ǵ���Ʒ����ҳ����������ȡ�õģ����ڲ�ѯ��Ʒ�����ʱ���Ѿ���redis����������Ʒ�Ļ��棬
		 * ����һ�����Դ�redis��ȡ�ø���Ʒ����
		 */
		String itemJson = jedisPoolDaoImpl.get(item + itemId);
		TbItemChild itemChild = JsonUtils.jsonToPojo(itemJson, TbItemChild.class);

		// ���ߵ�����˵��һ���Ѿ���¼���ˣ����Կ���ֱ��ȥredis���õ��Ǹ�TbUser����
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String userJson = jedisPoolDaoImpl.get(user + uuid);
		TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);

		String cartKey = cart + user.getId();

		if (jedisPoolDaoImpl.exists(cartKey)) {
			// ����û��ڻ����д��������ﳵ
			String cartJason = jedisPoolDaoImpl.get(cartKey);
			List<Cart> list = JsonUtils.jsonToList(cartJason, Cart.class);

			// ��ʼ�ж�����û��ڹ��ﳵ����û����ӹ������Ʒ
			int index = -1;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId() == itemId) {
					index = i;
					break;
				}
			}

			if (index == -1) {
				// ����û��ڹ��ﳵ�в�û����ӹ������Ʒ
				Cart cart = new Cart(itemChild.getId(), itemChild.getPrice(), itemChild.getTitle(), num,
						itemChild.getImages());
				list.add(cart);
			} else {
				// ����û��ڹ��ﳵ�в���ӹ������Ʒ
				Cart cart = list.get(index);
				cart.setNum(cart.getNum() + num);
			}
			jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));

		} else {
			// ����û���û���ڻ����д��������ﳵ
			List<Cart> list = new ArrayList<>();
			Cart cart = new Cart(itemChild.getId(), itemChild.getPrice(), itemChild.getTitle(), num,
					itemChild.getImages());
			list.add(cart);
			jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));
		}
	}

	@Override
	public List<Cart> showCart(HttpServletRequest request) {
		// ��redis�л�ȡ�Ѿ���ӵ�list
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

		// ��redis�л�ȡ�Ѿ���ӵ�list
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String userJson = jedisPoolDaoImpl.get(user + uuid);
		TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);

		String cartKey = cart + user.getId();

		String cartJason = jedisPoolDaoImpl.get(cartKey);
		List<Cart> list = JsonUtils.jsonToList(cartJason, Cart.class);

		// �޸�list�е��Ǹ���Ʒ������
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == itemId) {
				list.get(i).setNum(num);
				break;
			}
		}

		// �޸����������µ������ŵ�redis��
		String result = jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));

		// �ж�redis�Ƿ��޸ĳɹ�
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

		// ��redis�л�ȡ�Ѿ���ӵ�list
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

		// ɾ�������µ�list�ŵ�redis��
		String result = jedisPoolDaoImpl.set(cartKey, JsonUtils.objectToJson(list));

		// �ж�redis�Ƿ��޸ĳɹ�
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
