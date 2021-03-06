package com.jshoperx.action.frontstage.goods;

import com.jshoperx.action.backstage.base.DataCollectionTAction;
import com.jshoperx.action.backstage.staticspage.FreeMarkervariable;
import com.jshoperx.action.backstage.utils.BaseTools;
import com.jshoperx.action.backstage.utils.PageModel;
import com.jshoperx.action.backstage.utils.enums.BaseEnums;
import com.jshoperx.action.backstage.utils.statickey.StaticKey;
import com.jshoperx.action.frontstage.utils.RedisStaticKey;
import com.jshoperx.entity.*;
import com.jshoperx.redis.service.RedisBaseTService;
import com.jshoperx.redis.vo.*;
import com.jshoperx.service.*;
import com.jshoperx.vo.GoodsCategoryPathVo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("jshoperx")
@Namespace("")
@InterceptorRefs({ @InterceptorRef("defaultStack") })
public class GoodsCategoryAction extends ActionSupport {
	private GoodsTService goodsTService;
	private SiteNavigationTService siteNavigationTService;
	private GoodsCategoryTService goodsCategoryTService;
	private ArticleCategoryTService articleCategoryTService;
	private ArticleTService articleTService;
	private DataCollectionTAction dataCollectionTAction;
	private GoodsAttributeRpTService goodsAttributeRpTService;
	@SuppressWarnings("rawtypes")
	@Resource
	private RedisBaseTService redisBaseTService;
	private Map<String, Object> map = new HashMap<String, Object>();
	private List<GoodsT> goodsList;
	private String navid;
	private String ltypeid;
	private String stypeid;
	private String cp;
	private String ls;
	private String goodsname;
	private String topKeywords;
	private int rp = 8;// 每页显示数量
	private int page = 1;
	private int total = 0;
	private int totalpage = 1;
	private String attrs;// 用于查询的属性串
	private String grade;// 当前分类等级

	@JSON(serialize = false)
	public GoodsAttributeRpTService getGoodsAttributeRpTService() {
		return goodsAttributeRpTService;
	}

	public void setGoodsAttributeRpTService(
			GoodsAttributeRpTService goodsAttributeRpTService) {
		this.goodsAttributeRpTService = goodsAttributeRpTService;
	}

	@JSON(serialize = false)
	public DataCollectionTAction getDataCollectionTAction() {
		return dataCollectionTAction;
	}

	public void setDataCollectionTAction(
			DataCollectionTAction dataCollectionTAction) {
		this.dataCollectionTAction = dataCollectionTAction;
	}

	@JSON(serialize = false)
	public GoodsTService getGoodsTService() {
		return goodsTService;
	}

	public void setGoodsTService(GoodsTService goodsTService) {
		this.goodsTService = goodsTService;
	}

	@JSON(serialize = false)
	public SiteNavigationTService getSiteNavigationTService() {
		return siteNavigationTService;
	}

	public void setSiteNavigationTService(
			SiteNavigationTService siteNavigationTService) {
		this.siteNavigationTService = siteNavigationTService;
	}

	@JSON(serialize = false)
	public GoodsCategoryTService getGoodsCategoryTService() {
		return goodsCategoryTService;
	}

	public void setGoodsCategoryTService(
			GoodsCategoryTService goodsCategoryTService) {
		this.goodsCategoryTService = goodsCategoryTService;
	}

	@JSON(serialize = false)
	public ArticleCategoryTService getArticleCategoryTService() {
		return articleCategoryTService;
	}

	public void setArticleCategoryTService(
			ArticleCategoryTService articleCategoryTService) {
		this.articleCategoryTService = articleCategoryTService;
	}

	@JSON(serialize = false)
	public ArticleTService getArticleTService() {
		return articleTService;
	}

	public void setArticleTService(ArticleTService articleTService) {
		this.articleTService = articleTService;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<GoodsT> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsT> goodsList) {
		this.goodsList = goodsList;
	}

	public String getNavid() {
		return navid;
	}

	public void setNavid(String navid) {
		this.navid = navid;
	}

	public String getLtypeid() {
		return ltypeid;
	}

	public void setLtypeid(String ltypeid) {
		this.ltypeid = ltypeid;
	}

	public String getStypeid() {
		return stypeid;
	}

	public void setStypeid(String stypeid) {
		this.stypeid = stypeid;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getLs() {
		return ls;
	}

	public void setLs(String ls) {
		this.ls = ls;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public int getRp() {
		return rp;
	}

	public void setRp(int rp) {
		this.rp = rp;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getTopKeywords() {
		return topKeywords;
	}

	public void setTopKeywords(String topKeywords) {
		this.topKeywords = topKeywords;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * 清理错误
	 */
	@Override
	public void validate() {
		this.clearErrorsAndMessages();

	}

	/**
	 * 根据商品名称搜索商品
	 * 
	 * @return
	 */
	@Action(value = "searchGoodsByGoodsName", results = {
			@Result(name = "success", type = "freemarker", location = "/WEB-INF/theme/default/shop/searchgoods.ftl"),
			@Result(name = "input", type = "redirect", location = "/index.html") })
	public String searchGoodsByGoodsName() {
		List<GoodsT> list = null;
		int currentPage = page;
		int lineSize = rp;
		RedisGoodsVo rgv = (RedisGoodsVo) redisBaseTService.get(
				this.getTopKeywords(), RedisGoodsVo.class);
		if (rgv == null) {
			Criterion criterion= Restrictions.ilike("goodsname", this.getTopKeywords(), MatchMode.ANYWHERE);
			Order order=Order.desc("createtime");
			list=this.goodsTService.findByCriteriaByPage(GoodsT.class, criterion, order, currentPage, lineSize);
			rgv = new RedisGoodsVo();
			rgv.setGlist(list);
			redisBaseTService.put(this.getTopKeywords(), rgv,
					RedisGoodsVo.class);
		} else {
			list = rgv.getGlist();
		}
		if (list != null && list.size() > 0) {
			Criterion criterion1=Restrictions.and(Restrictions.eq("salestate", BaseEnums.GoodsSaleState.SALE.getState())).add(Restrictions.ilike("goodsname", this.getTopKeywords().trim(), MatchMode.ANYWHERE));
			total=this.goodsTService.count(GoodsT.class, criterion1).intValue();
			PageModel<GoodsT> pm = new PageModel<GoodsT>(currentPage, lineSize,
					list, total);
			String action = BaseTools.getBasePath()
					+ "/searchGoodsByGoodsName.action?topKeywords="
					+ this.getTopKeywords();
			ActionContext.getContext().put("actionlink", action);
			ActionContext.getContext().put("sign", "disstatic");
			ActionContext.getContext().put("goods", pm);
			ActionContext.getContext().put("goodsList", list);
			ActionContext.getContext().put("totalgoods", pm.getTotalRecord());
			ActionContext.getContext().put("totalpage", pm.getTotalpage());
			ActionContext.getContext().put("topKeywords", topKeywords);

		}

		this.getDataCollectionTAction().putBaseInfoToContext();

		return SUCCESS;

	}

	/**
	 * 根据属性获取商品列表用于属性搜索
	 * 
	 * @return
	 */
	@Action(value = "searchGoodsByAttrs", results = {
			@Result(name = "success", type = "freemarker", location = "/WEB-INF/theme/default/shop/goodscategorylist.ftl"),
			@Result(name = "input", type = "redirect", location = "/index.html") })
	public String findGoodsByAttrs() {
		if (StringUtils.isNotBlank(this.getAttrs())) {
			int currentPage = page;
			int lineSize = rp;
			List<GoodsT> goodslist = new ArrayList<GoodsT>();
			String strs[] = StringUtils.split(this.getAttrs(),
					StaticKey.SPLITDOT);
			if (strs[0].equals(StaticKey.ALLATTRS)) {
				if (this.getGrade().equals(StaticKey.ZERO)) {
					Map<String,String>params=new HashMap<>();
					params.put("salestate",BaseEnums.GoodsSaleState.SALE.getState());
					params.put("navid",this.getNavid());
					Criterion criterion=Restrictions.allEq(params);
					total = this.goodsTService.count(GoodsT.class,criterion).intValue();
					goodslist = this.goodsTService.findByCriteriaByPage(GoodsT.class,criterion,null,currentPage,lineSize);
				}
			} else {
				for (String s : strs) {
					Criterion criterion=Restrictions.eq("attrval",s);

					List<GoodsAttributeRpT> garptlist = this.goodsAttributeRpTService.findByCriteria(GoodsAttributeRpT.class,criterion);
					goodslist.addAll(findAttrGoodsInfo(garptlist));
				}
				total = goodslist.size();
			}

			PageModel<GoodsT> pm = new PageModel<GoodsT>(currentPage, lineSize,
					goodslist, total);
			String action = "";
			ActionContext.getContext().put("actionlink", action);
			ActionContext.getContext().put("sign", "disstatic");
			ActionContext.getContext().put("goods", pm);
			ActionContext.getContext().put("goodsList", goodslist);
			ActionContext.getContext().put("totalgoods", pm.getTotalRecord());
			ActionContext.getContext().put("totalpage", pm.getTotalpage());
			ActionContext.getContext().put("topKeywords", topKeywords);
			// 将商品分类put到上下文

			GoodsCategoryT gct = this.getDataCollectionTAction()
					.getGoodsCategoryTByNavid(this.getNavid());
			ActionContext.getContext().put(FreeMarkervariable.GOODSCATEGORY,
					gct);
			ActionContext.getContext().put(
					FreeMarkervariable.GOODSCATEGORYPATH,
					this.getDataCollectionTAction().getGoodsCategoryPath(
							gct.getPath()));
			ActionContext.getContext()
					.put(FreeMarkervariable.GOODSTYPEBRAND,
							this.getDataCollectionTAction()
									.findGoodsTypeBrandBygoodsType(
											gct.getGoodsTypeId()));
			ActionContext.getContext().put(
					FreeMarkervariable.GOODSATTRS,
					this.getDataCollectionTAction()
							.findGoodsAttributeTBygoodsTypeId(
									gct.getGoodsTypeId()));// 收集商品类型下的商品属性
			ActionContext.getContext().put(
					FreeMarkervariable.SECONDGOODSCATEGORY,
					this.getDataCollectionTAction().findSecondGoodsCategoryT(
							gct.getGoodsCategoryTid(),
							BaseEnums.DataUsingState.USING.getState()));// 获取此分类下的二级分类

		}
		this.getDataCollectionTAction().putBaseInfoToContext();
		return SUCCESS;
	}

	private List<GoodsT> findAttrGoodsInfo(List<GoodsAttributeRpT> list) {
		List<GoodsT> lists = new ArrayList<GoodsT>();
		for (GoodsAttributeRpT ga : list) {
			GoodsT g = this.getGoodsTService().findByPK(GoodsT.class, ga.getGoodsid());
			if (g != null) {
				lists.add(g);
			}
		}
		return lists;
	}

	/**
	 * 构建一级分类商品页面
	 * 
	 * @return
	 */
	@Action(value = "findFirstCategoryTGoods", results = {
			@Result(name = "success", type = "freemarker", location = "/WEB-INF/theme/default/shop/goodscategorylist.ftl"),
			@Result(name = "input", type = "redirect", location = "/index.html") })
	public String findFirstCategoryTGoodsPage() {
		// 获取一级商品分类数据
		getFirstGoodsCategoryT();
		//获取一级商品分类页面其余关键业务数据
		getFirstGoodsCategoryPageAttrInfo();
		this.getDataCollectionTAction().putBaseInfoToContext();
		return SUCCESS;
	}

	/**
	 * 获取一级分类页面中业务元素信息
	 */
	private void getFirstGoodsCategoryPageAttrInfo() {
		GoodsCategoryT gct = null;
		RedisFirstGoodsCateogryTVo firstGoodsCateogryTVo = (RedisFirstGoodsCateogryTVo) redisBaseTService
				.get(RedisStaticKey.GOODSCATEGORY + this.getNavid(),
						RedisFirstGoodsCateogryTVo.class);
		if (firstGoodsCateogryTVo == null) {
			gct = this.getGoodsCategoryTService().findByPK(
					GoodsCategoryT.class, this.getNavid());
			if (gct != null) {
				firstGoodsCateogryTVo = new RedisFirstGoodsCateogryTVo();
				firstGoodsCateogryTVo.setGoodsCategoryTs(gct);
			}
		} else {
			gct = firstGoodsCateogryTVo.getGoodsCategoryTs();
		}
		// goodscategorypath
		List<GoodsCategoryPathVo> gcpath = null;
		RedisGoodsCategoryPathVo rgv = (RedisGoodsCategoryPathVo) redisBaseTService
				.get(RedisStaticKey.GOODSCATEGORYPATH + this.getNavid(),
						RedisGoodsCategoryPathVo.class);
		if (rgv == null) {
			gcpath = this.getDataCollectionTAction().getGoodsCategoryPath(
					gct.getPath());
			rgv = new RedisGoodsCategoryPathVo();
			rgv.setGcpath(gcpath);
		} else {
			gcpath = rgv.getGcpath();
		}
		ActionContext.getContext().put(FreeMarkervariable.GOODSCATEGORYPATH,
				gcpath);
		// goodstypebrand
		List<GoodsTypeBrandT> gtbrand = null;
		RedisGoodsTypeBrandVo rgvbrand = (RedisGoodsTypeBrandVo) redisBaseTService
				.get(RedisStaticKey.GOODSTYPEBRAND + this.getNavid(),
						RedisGoodsTypeBrandVo.class);
		if (rgvbrand == null) {
			gtbrand = this.getDataCollectionTAction()
					.findGoodsTypeBrandBygoodsType(gct.getGoodsTypeId());// 收集商品类型下的品牌
			rgvbrand = new RedisGoodsTypeBrandVo();
			rgvbrand.setGtbrand(gtbrand);
		} else {
			gtbrand = rgvbrand.getGtbrand();
		}
		ActionContext.getContext().put(FreeMarkervariable.GOODSTYPEBRAND,
				gtbrand);
		// goodsattrs
		List<GoodsAttributeT> gattrAttributeTs = null;
		RedisGoodsAttrsVo goodsAttrsVo = (RedisGoodsAttrsVo) redisBaseTService
				.get(RedisStaticKey.GOODSATTRS + this.getNavid(),
						RedisGoodsAttrsVo.class);
		if (goodsAttrsVo == null) {
			gattrAttributeTs = this.getDataCollectionTAction()
					.findGoodsAttributeTBygoodsTypeId(gct.getGoodsTypeId());// 收集商品类型下的商品属性
			goodsAttrsVo = new RedisGoodsAttrsVo();
			goodsAttrsVo.setGoodsAttributeT(gattrAttributeTs);
		} else {
			gattrAttributeTs = goodsAttrsVo.getGoodsAttributeT();
		}
		ActionContext.getContext().put(FreeMarkervariable.GOODSATTRS,
				gattrAttributeTs);
		// secondgoodscateogry
		List<GoodsCategoryT> secondGoodsCategoryTs = null;
		RedisSecondGoodsCateogryTVo secondGoodsCateogryTVo = (RedisSecondGoodsCateogryTVo) redisBaseTService
				.get(RedisStaticKey.GOODSATTRS + this.getNavid(),
						RedisSecondGoodsCateogryTVo.class);
		if (secondGoodsCateogryTVo == null) {
			secondGoodsCategoryTs = this.getDataCollectionTAction()
					.findSecondGoodsCategoryT(gct.getGoodsCategoryTid(),
							BaseEnums.DataUsingState.USING.getState());// 获取此分类下的二级分类
			secondGoodsCateogryTVo = new RedisSecondGoodsCateogryTVo();
			secondGoodsCateogryTVo.setGoodsCategoryTs(secondGoodsCategoryTs);
		} else {
			secondGoodsCategoryTs = secondGoodsCateogryTVo.getGoodsCategoryTs();
		}
		ActionContext.getContext().put(FreeMarkervariable.SECONDGOODSCATEGORY,
				secondGoodsCategoryTs);
		ActionContext.getContext().put(FreeMarkervariable.GOODSCATEGORY, gct);
		//hotsale goods
		List<GoodsT> hotsaleGoodsTs = null;
		RedisHotsaleGoodsVo hotsaleGoodsVo = (RedisHotsaleGoodsVo) redisBaseTService
				.get(RedisStaticKey.HOTSALEGOODS + this.getNavid(),
						RedisHotsaleGoodsVo.class);
		if (hotsaleGoodsVo == null) {
			//hotsaleGoodsTs = this.getDataCollectionTAction().getHostsaleGoodsByCategoryId(gct.getGoodsCategoryTid(), null);
			hotsaleGoodsVo = new RedisHotsaleGoodsVo();
			hotsaleGoodsVo.setHotsaleGoodsTs(hotsaleGoodsTs);
		} else {
			hotsaleGoodsTs = hotsaleGoodsVo.getHotsaleGoodsTs();
		}
		ActionContext.getContext().put(FreeMarkervariable.HOTSALEGOODSLIST,
				hotsaleGoodsTs);
		
	}

	/**
	 * 获取一级分类商品数据
	 */
	private void getFirstGoodsCategoryT() {
		List<GoodsT> list = null;
		int currentPage = page;
		int lineSize = rp;
		RedisGoodsInFirstCateogryTVo rgv = (RedisGoodsInFirstCateogryTVo) redisBaseTService
				.get(RedisStaticKey.FIRSTCATEGORYGOODS + this.getNavid(),
						RedisGoodsInFirstCateogryTVo.class);
		if (rgv == null) {
			Map<String,String>params=new HashMap<>();
			params.put("navid",this.getNavid());
			params.put("salestate",BaseEnums.GoodsSaleState.SALE.getState());
			Criterion criterion=Restrictions.allEq(params);
			Order order=Order.desc("createtime");
			list =this.goodsTService.findByCriteriaByPage(GoodsT.class,criterion,order,currentPage,lineSize);
			rgv = new RedisGoodsInFirstCateogryTVo();
			rgv.setGlist(list);
			redisBaseTService.put(
					RedisStaticKey.FIRSTCATEGORYGOODS + this.getNavid(), rgv,
					RedisGoodsVo.class);
		} else {
			list = rgv.getGlist();
		}
		if (list != null && list.size() > 0) {
			Map<String,String>params=new HashMap<>();
			params.put("salestate",BaseEnums.GoodsSaleState.SALE.getState());
			params.put("navid",this.getNavid());
			Criterion criterion=Restrictions.allEq(params);
			total = this.goodsTService.count(GoodsT.class, criterion).intValue();
		}
		PageModel<GoodsT> pm = new PageModel<GoodsT>(currentPage, lineSize,
				list, total);
		String action = BaseTools.getBasePath()
				+ "/findFirstCategoryTGoods.action?navid=" + this.getNavid();
		ActionContext.getContext().put("actionlink", action);
		ActionContext.getContext().put("sign", "disstatic");
		ActionContext.getContext().put("goods", pm);
		ActionContext.getContext().put("goodsList", list);
		ActionContext.getContext().put("totalgoods", pm.getTotalRecord());
		ActionContext.getContext().put("totalpage", pm.getTotalpage());

	}

	
	
	/**
	 * 构建二级分类商品页面
	 * 
	 * @return
	 */
	@Action(value = "findSecondCategoryTGoods", results = {
			@Result(name = "success", type = "freemarker", location = "/WEB-INF/theme/default/shop/goodssecondcategorylist.ftl"),
			@Result(name = "input", type = "redirect", location = "/index.html") })
	public String findSecondCategoryTGoodsPage() {
		// 获取一级商品分类数据
		getSecondGoodsCategoryT();
		//获取一级商品分类页面其余关键业务数据
		getSecondGoodsCategoryPageAttrInfo();
		this.getDataCollectionTAction().putBaseInfoToContext();
		return SUCCESS;
	}
	/**
	 * 获取二级分类页面中业务元素信息
	 */
	private void getSecondGoodsCategoryPageAttrInfo() {
		GoodsCategoryT gct = null;
		RedisFirstGoodsCateogryTVo firstGoodsCateogryTVo = (RedisFirstGoodsCateogryTVo) redisBaseTService
				.get(RedisStaticKey.GOODSCATEGORY + this.getLtypeid(),
						RedisFirstGoodsCateogryTVo.class);
		if (firstGoodsCateogryTVo == null) {
			gct = this.getGoodsCategoryTService().findByPK(
					GoodsCategoryT.class, this.getLtypeid());
			if (gct != null) {
				firstGoodsCateogryTVo = new RedisFirstGoodsCateogryTVo();
				firstGoodsCateogryTVo.setGoodsCategoryTs(gct);
			}
		} else {
			gct = firstGoodsCateogryTVo.getGoodsCategoryTs();
		}
		// goodscategorypath
		List<GoodsCategoryPathVo> gcpath = null;
		RedisGoodsCategoryPathVo rgv = (RedisGoodsCategoryPathVo) redisBaseTService
				.get(RedisStaticKey.GOODSCATEGORYPATH + this.getLtypeid(),
						RedisGoodsCategoryPathVo.class);
		if (rgv == null) {
			gcpath = this.getDataCollectionTAction().getGoodsCategoryPath(
					gct.getPath());
			rgv = new RedisGoodsCategoryPathVo();
			rgv.setGcpath(gcpath);
		} else {
			gcpath = rgv.getGcpath();
		}
		ActionContext.getContext().put(FreeMarkervariable.GOODSCATEGORYPATH,
				gcpath);
		// goodstypebrand
		List<GoodsTypeBrandT> gtbrand = null;
		RedisGoodsTypeBrandVo rgvbrand = (RedisGoodsTypeBrandVo) redisBaseTService
				.get(RedisStaticKey.GOODSTYPEBRAND + this.getLtypeid(),
						RedisGoodsTypeBrandVo.class);
		if (rgvbrand == null) {
			gtbrand = this.getDataCollectionTAction()
					.findGoodsTypeBrandBygoodsType(gct.getGoodsTypeId());// 收集商品类型下的品牌
			rgvbrand = new RedisGoodsTypeBrandVo();
			rgvbrand.setGtbrand(gtbrand);
		} else {
			gtbrand = rgvbrand.getGtbrand();
		}
		ActionContext.getContext().put(FreeMarkervariable.GOODSTYPEBRAND,
				gtbrand);
		// goodsattrs
		List<GoodsAttributeT> gattrAttributeTs = null;
		RedisGoodsAttrsVo goodsAttrsVo = (RedisGoodsAttrsVo) redisBaseTService
				.get(RedisStaticKey.GOODSATTRS + this.getLtypeid(),
						RedisGoodsAttrsVo.class);
		if (goodsAttrsVo == null) {
			gattrAttributeTs = this.getDataCollectionTAction()
					.findGoodsAttributeTBygoodsTypeId(gct.getGoodsTypeId());// 收集商品类型下的商品属性
			goodsAttrsVo = new RedisGoodsAttrsVo();
			goodsAttrsVo.setGoodsAttributeT(gattrAttributeTs);
		} else {
			gattrAttributeTs = goodsAttrsVo.getGoodsAttributeT();
		}
		ActionContext.getContext().put(FreeMarkervariable.GOODSATTRS,
				gattrAttributeTs);
		// secondgoodscateogry
		List<GoodsCategoryT> secondGoodsCategoryTs = null;
		RedisSecondGoodsCateogryTVo secondGoodsCateogryTVo = (RedisSecondGoodsCateogryTVo) redisBaseTService
				.get(RedisStaticKey.GOODSATTRS + this.getLtypeid(),
						RedisSecondGoodsCateogryTVo.class);
		if (secondGoodsCateogryTVo == null) {
			secondGoodsCategoryTs = this.getDataCollectionTAction()
					.findSecondGoodsCategoryT(gct.getGoodsCategoryTid(),
							BaseEnums.DataUsingState.USING.getState());// 获取此分类下的二级分类
			secondGoodsCateogryTVo = new RedisSecondGoodsCateogryTVo();
			secondGoodsCateogryTVo.setGoodsCategoryTs(secondGoodsCategoryTs);
		} else {
			secondGoodsCategoryTs = secondGoodsCateogryTVo.getGoodsCategoryTs();
		}
		ActionContext.getContext().put(FreeMarkervariable.SECONDGOODSCATEGORY,
				secondGoodsCategoryTs);
		ActionContext.getContext().put(FreeMarkervariable.GOODSCATEGORY, gct);
		//hotsale goods
		List<GoodsT> hotsaleGoodsTs = null;
		RedisHotsaleGoodsVo hotsaleGoodsVo = (RedisHotsaleGoodsVo) redisBaseTService
				.get(RedisStaticKey.HOTSALEGOODS + this.getLtypeid(),
						RedisHotsaleGoodsVo.class);
		if (hotsaleGoodsVo == null) {
			//hotsaleGoodsTs = this.getDataCollectionTAction().getHostsaleGoodsByCategoryId(gct.getGoodsCategoryTid(), null);
			hotsaleGoodsVo = new RedisHotsaleGoodsVo();
			hotsaleGoodsVo.setHotsaleGoodsTs(hotsaleGoodsTs);
		} else {
			hotsaleGoodsTs = hotsaleGoodsVo.getHotsaleGoodsTs();
		}
		ActionContext.getContext().put(FreeMarkervariable.HOTSALEGOODSLIST,
				hotsaleGoodsTs);
		
	}

	/**
	 * 获取二级分类商品数据
	 */
	private void getSecondGoodsCategoryT() {
		List<GoodsT> list =null;
		int currentPage = page;
		int lineSize = rp;
		RedisGoodsInFirstCateogryTVo rgv = (RedisGoodsInFirstCateogryTVo) redisBaseTService
				.get(RedisStaticKey.SECONDCATEGORYGOODS + this.getLtypeid(),
						RedisGoodsInFirstCateogryTVo.class);
		if (rgv == null) {
			Map<String,String>params=new HashMap<>();
			params.put("ltypeid",this.getLtypeid());
			params.put("salestate",BaseEnums.GoodsSaleState.SALE.getState());
			Criterion criterion=Restrictions.allEq(params);
			Order order=Order.desc("createtime");
			list = this.goodsTService.findByCriteriaByPage(GoodsT.class,criterion,order,currentPage,lineSize);
			rgv = new RedisGoodsInFirstCateogryTVo();
			rgv.setGlist(list);
			redisBaseTService.put(
					RedisStaticKey.SECONDCATEGORYGOODS + this.getLtypeid(), rgv,
					RedisGoodsVo.class);
		} else {
			list = rgv.getGlist();
		}
		if (list != null && list.size() > 0) {
			Map<String,String>params=new HashMap<>();
			params.put("salestate",BaseEnums.GoodsSaleState.SALE.getState());
			params.put("navid",this.getNavid());
			Criterion criterion=Restrictions.allEq(params);
			total = this.goodsTService.count(GoodsT.class, criterion).intValue();

		}
		PageModel<GoodsT> pm = new PageModel<GoodsT>(currentPage, lineSize,
				list, total);
		String action = BaseTools.getBasePath()
				+ "/findSecondCategoryTGoods.action?navid=" + this.getNavid();
		ActionContext.getContext().put("actionlink", action);
		ActionContext.getContext().put("sign", "disstatic");
		ActionContext.getContext().put("goods", pm);
		ActionContext.getContext().put("goodsList", list);
		ActionContext.getContext().put("totalgoods", pm.getTotalRecord());
		ActionContext.getContext().put("totalpage", pm.getTotalpage());

	}
}
