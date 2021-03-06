package com.jshoperx.action.backstage.authority;

import com.jshoperx.action.backstage.base.BaseTAction;
import com.jshoperx.action.backstage.utils.BaseTools;
import com.jshoperx.action.backstage.utils.statickey.StaticKey;
import com.jshoperx.entity.ModuleM;
import com.jshoperx.service.BaseTService;
import com.jshoperx.service.impl.Serial;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.annotation.Resource;
import java.util.*;
@Namespace("")
@ParentPackage("jshoperx")
public class ModuleMAction extends BaseTAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private BaseTService<ModuleM>baseTService;
	private String id;
	private String modulename;
	private Date createtime;
	private String moduleselectstr;
	private ModuleM bean=new ModuleM();
	private Map<String,Object>map=new HashMap<String,Object>();
	private List<Map<String, Object>> rows=new ArrayList<Map<String, Object>>();
	private int rp;
	private int page = 1;
	private int total = 0;
	private String query;
	private String qtype;
	private boolean sucflag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModulename() {
		return modulename;
	}
	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public ModuleM getBean() {
		return bean;
	}
	public void setBean(ModuleM bean) {
		this.bean = bean;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	public List<Map<String, Object>> getRows() {
		return rows;
	}
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
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
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getQtype() {
		return qtype;
	}
	public void setQtype(String qtype) {
		this.qtype = qtype;
	}
	public boolean isSucflag() {
		return sucflag;
	}
	public void setSucflag(boolean sucflag) {
		this.sucflag = sucflag;
	}
	public String getModuleselectstr() {
		return moduleselectstr;
	}
	public void setModuleselectstr(String moduleselectstr) {
		this.moduleselectstr = moduleselectstr;
	}
	/**
	 * 清理错误
	 */
	@Override
	public void validate() {
		this.clearErrorsAndMessages();

	}
	
	/**
	 * 增加模块
	 * @return
	 */
	@Action(value = "addModuleM", results = { @Result(name = "json", type = "json") })
	public String addModuleM(){
		ModuleM mm=new ModuleM();
		mm.setId(this.getSerial().Serialid(Serial.MODULE));
		mm.setModulename(this.getModulename());
		mm.setCreatetime(BaseTools.getSystemTime());
		this.baseTService.save(mm);
		this.setSucflag(true);
		return "json";
	}
	
	/**
	 * 根据id获取模块
	 * @return
	 */
	@Action(value = "findModuleMById", results = { @Result(name = "json", type = "json") })
	public String findModuleMById(){
		if(StringUtils.isNotBlank(this.getId())){
			bean=this.baseTService.findByPK(ModuleM.class, this.getId());
			if(bean!=null){
				this.setSucflag(true);
				return "json";
			}
			this.setSucflag(false);
			return "json";
		}
		this.setSucflag(false);
		return "json";
	}
	
	/**
	 * 更新模块
	 * @return
	 */
	@Action(value = "updateModuleM", results = { @Result(name = "json", type = "json") })
	public String updateModuleM(){
		bean.setId(this.getId());
		bean.setModulename(this.getModulename());
		bean.setCreatetime(BaseTools.getSystemTime());
		this.baseTService.update(bean);
		this.setSucflag(true);
		return "json";
	}
	
	/**
	 * 查询所有模块
	 * @return
	 */
	@Action(value = "findAllModuleM", results = { @Result(name = "json", type = "json") })	
	public String findAllModuleM(){
		if(StaticKey.SC.equals(this.getQtype())){
			this.findDefaultAllModuleM();
		}else{
			return "json";
		}
		return "json";
	}
	
	public void findDefaultAllModuleM(){
		List<ModuleM>list=this.baseTService.findAll(ModuleM.class);
		if(!list.isEmpty()){
			total=list.size();
			this.ProcessModuleMList(list);
		}
	}
	
	public void ProcessModuleMList(List<ModuleM>list){
		for(Iterator<ModuleM> it=list.iterator();it.hasNext();){
			ModuleM mm=(ModuleM)it.next();
			Map<String,Object>cellMap=new HashMap<String,Object>();
			cellMap.put("id", mm.getId());
			cellMap.put("cell", new Object[]{
				mm.getModulename(),
				BaseTools.formateDbDate(mm.getCreatetime()),
				"<a id='editmodule' name='editmodule' href='module.jsp?operate=edit&folder=setting&id="+mm.getId()+"'>[编辑]</a>"
			});
			rows.add(cellMap);
		}
	}
	
	/**
	 * 批量删除模块
	 * @return
	 */
	@Action(value = "delModuleM", results = { @Result(name = "json", type = "json") })
	public String delModuleM(){
		if(StringUtils.isNotBlank(this.getId())){
			String []strs=StringUtils.split(this.getId(), StaticKey.SPLITDOT);
			for(String s:strs){
				ModuleM mm=this.baseTService.findByPK(ModuleM.class, s);
				if(mm!=null){
					this.baseTService.delete(mm);
				}
			}
			this.setSucflag(true);
			return "json";
		}
		this.setSucflag(false);
		return "json";
	}
	
	/**
	 * 获取所有的模块用于前台select控件显示
	 * @return
	 */
	@Action(value = "findAllModuleForselect", results = { @Result(name = "json", type = "json") })
	public String findAllModuleForselect(){
		this.setModuleselectstr("");
		List<ModuleM>list=this.baseTService.findAll(ModuleM.class);
		if(!list.isEmpty()){
			this.setModuleselectstr("<option value='0'>---请选择---</option>");
			for(Iterator<ModuleM> it=list.iterator();it.hasNext();){
				ModuleM mm=(ModuleM)it.next();
				this.moduleselectstr+="<option value='"+mm.getId()+"'>"+mm.getModulename()+"</option>";
			}
			this.setSucflag(true);
			return "json";
		}
		this.setSucflag(true);
		return "json";
	}
	
	
}
