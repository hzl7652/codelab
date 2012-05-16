package sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.poi.hssf.model.RecordStream;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.lang.Strings;

public class Wordpress2Cms {
	static NutDao dao_remote = new NutDao();
	static NutDao dao_local = new NutDao();
	static void  init() throws Exception{

		Properties pp = new Properties();
		dao_remote = new NutDao();
		pp.put("url","jdbc:mysql://localhost:3306/feiyanco_wordpress?useUnicode=true&characterEncoding=utf-8" );
		pp.put("driverClassName", "com.mysql.jdbc.Driver");
		pp.put("username", "root");
		pp.put("password", "000000");
		DataSource ds = BasicDataSourceFactory.createDataSource(pp);
		dao_remote.setDataSource(ds);
		
		pp = new Properties();
		dao_local = new NutDao();
		pp.put("url","jdbc:h2:D:/var/procdb" );
		pp.put("driverClassName", "org.h2.Driver");
		pp.put("username", "sa");
		pp.put("password", "");
		DataSource ds1 = BasicDataSourceFactory.createDataSource(pp);
		dao_local.setDataSource(ds1);
		
	}
	static Map<String ,Integer> cats = new HashMap<String, Integer>();
	static Map<String ,Integer> tags = new HashMap<String, Integer>();
	static List<Record>  tagCats = null;
	public static void main(String[] args) throws Exception {
		init();
		Sql postSql = Sqls.create("select wp_posts.id as id, post_date,post_title,post_content,group_concat('<br>',comment_date ,':<br>',comment_content) as comments from wp_posts left join wp_comments on wp_posts.id = wp_comments.comment_post_ID where (comment_approved='1' or comment_approved is null) and post_status='publish' and post_type='post'  group by wp_posts.id order by post_date").setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet arg1, Sql arg2)
					throws SQLException {
				List<Record>  records = new ArrayList<Record>();
				while(arg1.next()){
					Record r = new Record();
					r.set("id", arg1.getInt("id"));
					r.set("date",arg1.getTimestamp("post_date"));
					r.set("title",arg1.getString("post_title"));
					r.set("content",arg1.getString("post_content"));
					r.set("comments",arg1.getString("comments"));
					System.out.println(arg1.getTimestamp("post_date"));
					records.add(r);
				}
				return records;
			}
		});
		dao_remote.execute(postSql);
		List<Record> posts = postSql.getList(Record.class); 
		System.out.println("post size:"+posts.size());
		
		
		List<Record> tagsR = dao_local.query("tag", null, null);
		List<Record> catsR = dao_local.query("category", null, null);
		System.out.println("tags size:"+tagsR.size());
		System.out.println("cats size:"+catsR.size());
		for(Record r : tagsR){
			tags.put(r.getString("name"), r.getInt("id"));
		}
		for(Record r : catsR){
			cats.put(r.getString("name"), r.getInt("id"));
		}
		Sql tagCatSql = Sqls.create("select p.id as id ,t.name as name,tt.taxonomy as type from wp_posts as p ,wp_term_relationships as tr ,wp_term_taxonomy as tt , wp_terms as t where p.id = tr.object_id and tr.term_taxonomy_id = tt.term_taxonomy_id and tt.term_id = t.term_id and taxonomy in ('category','post_tag') order by p.id").setCallback(new SqlCallback() {
			
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				List<Record> records = new ArrayList<Record>();
				while (rs.next()){
					records.add(Record.create(rs));
				}
				return records;
			}
		});
		dao_remote.execute(tagCatSql);
		tagCats = tagCatSql.getList(Record.class);
		System.out.println("tagCats size:"+tagCats.size());
		
		for(int i=0;i<posts.size();i++){
			Record rr = posts.get(i);
			dao_local.insert("news", Chain.make("title", rr.getString("title")).add("content", rr.getString("content")+(Strings.isEmpty(rr.getString("comments"))?"":rr.getString("comments"))).add("create_time", rr.getTimestamp("date")));
			Integer newsOldId = rr.getInt("id");
			Integer newsId = dao_local.fetch("news", Cnd.orderBy().desc("id")).getInt("id");
			List<Record> tagCatList = findTagCat(newsOldId);
			for(Record rt : tagCatList){
				if("post_tag".equals(rt.getString("type"))){
					Integer tagId = getTagId(rt.getString("name"));
					dao_local.insert("t_news_tag", Chain.make("news_id", newsId).add("tag_id", tagId));
				}else{
					Integer catId = getCatId(rt.getString("name"));
					dao_local.insert("t_news_category", Chain.make("news_id", newsId).add("category_id", catId));
				}
			}
		}
		
	}
	
	static Integer getTagId(String name ){
		Integer id = tags.get(name);
		if(id == null){
			dao_local.insert("tag", Chain.make("name", name));
			id = dao_local.fetch("tag", Cnd.orderBy().desc("id")).getInt("id");
			tags.put(name, id);
		}
		return id;
	}
	static Integer getCatId(String name ){
		Integer id = cats.get(name);
		if(id == null){
			dao_local.insert("category", Chain.make("name", name));
			id = dao_local.fetch("category", Cnd.orderBy().desc("id")).getInt("id");
			cats.put(name, id);
		}
		return id;
	}
	static List<Record> findTagCat(Integer postId){
		List<Record> results = new ArrayList<Record>();
		for(Record rr:tagCats){
			if(postId.equals(rr.getInt("id"))){
				results.add(rr);
			}
		}
		return results;
	}
}
