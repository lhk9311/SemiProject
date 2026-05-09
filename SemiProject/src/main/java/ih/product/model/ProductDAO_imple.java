package ih.product.model;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import javax.sql.DataSource;

import ih.product.domain.CartDTO;
import ih.product.domain.ProductDTO;

public class ProductDAO_imple implements ProductDAO {

    private DataSource ds; 
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    // 생성자 (커넥션 풀 설정)
    public ProductDAO_imple() {
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            ds = (DataSource)envContext.lookup("SemiProject"); // context.xml의 name과 일치해야 함
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // 자원 반납 메소드
    private void close() {
        try {
            if(rs != null)    {rs.close(); rs=null;}
            if(pstmt != null) {pstmt.close(); pstmt=null;}
            if(conn != null)  {conn.close(); conn=null;}
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // 페이징 처리를 한 카테고리별 상품 조회
    @Override
    public List<ProductDTO> selectProductByCategory(String categoryId, String userid) throws SQLException {
        
        List<ProductDTO> productList = new ArrayList<>();
        
        try {
            conn = ds.getConnection();
            
            String sql = " SELECT product_id, product_name, pimage, sale_price, list_price, stock, fk_category_id, "
            		   + " (SELECT count(*) FROM tbl_wishlist WHERE product_id = P.product_id AND member_id = ?) AS is_wish "
            		   + " FROM tbl_product P "
                       + " WHERE fk_category_id = ? " // 카테고리 필터링
                       + " ORDER BY product_id DESC ";
            
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, userid); 
            pstmt.setInt(2, Integer.parseInt(categoryId)); 
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                ProductDTO pdto = new ProductDTO();
                pdto.setProduct_id(rs.getInt("product_id"));
                pdto.setProduct_name(rs.getString("product_name"));
                pdto.setPimage(rs.getString("pimage"));
                pdto.setSale_price(rs.getInt("sale_price"));
                pdto.setList_price(rs.getInt("list_price"));
                pdto.setStock(rs.getInt("stock"));
                pdto.setFk_category_id(rs.getInt("fk_category_id"));
                pdto.setIs_wish(rs.getInt("is_wish"));
                productList.add(pdto);
            }
        } finally {
            close();
        }
        return productList;
    }

    // 상품 상세 정보 조회
 // 상품 상세 정보 조회
    @Override
    public ProductDTO selectOneProduct(String productId, String userid) throws SQLException {
        ProductDTO pdto = null;
        try {
            conn = ds.getConnection();
            
            String sql = " SELECT P.*,"
                       + " (SELECT count(*) FROM tbl_wishlist WHERE product_id = P.product_id AND member_id = ?) AS is_wish "
                       + " FROM tbl_product P "
                       + " WHERE P.product_id = ? ";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userid); 
            pstmt.setString(2, productId);
            
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
                pdto = new ProductDTO();
                pdto.setProduct_id(rs.getInt("product_id"));
                pdto.setProduct_name(rs.getString("product_name"));
                pdto.setFk_category_id(rs.getInt("fk_category_id"));
                pdto.setSale_price(rs.getInt("sale_price"));
                pdto.setList_price(rs.getInt("list_price"));
                pdto.setStock(rs.getInt("stock"));
                pdto.setFk_spec_id(rs.getInt("fk_spec_id"));
                pdto.setProduct_description(rs.getString("product_description"));
                pdto.setPimage(rs.getString("pimage"));
                pdto.setPstatus(rs.getInt("pstatus")); 
                pdto.setIs_wish(rs.getInt("is_wish"));
            }
        } finally {
            close();
        }
        return pdto;
    } // end of public ProductDTO selectOneProduct(String productId) throws SQLException ----

    // 상품 등록 (테이블 설계 컬럼명 반영)
    @Override
    public int productInsert(ProductDTO dto) throws SQLException {
        int result = 0;
        try {
            conn = ds.getConnection();
            
            // SQL문의 순서: (1)name, (2)category, (3)pimage, (4)sale_price, (5)list_price, (6)stock, (7)fk_spec_id, (8)description
            String sql = " INSERT INTO tbl_product (product_id, product_name, fk_category_id, pimage, "
                       + " sale_price, list_price, stock, fk_spec_id, product_description, stock_date) "
                       + " VALUES (seq_product.nextval, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ";
            
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, dto.getProduct_name());      // (1) name
            pstmt.setInt(2, dto.getFk_category_id());       // (2) category_id
            pstmt.setString(3, dto.getPimage());            // (3) pimage (String)
            pstmt.setInt(4, dto.getSale_price());           // (4) sale_price
            pstmt.setInt(5, dto.getList_price());           // (5) list_price
            pstmt.setInt(6, dto.getStock());                // (6) stock
            pstmt.setInt(7, dto.getFk_spec_id());           // (7) fk_spec_id (중요!)
            pstmt.setString(8, dto.getProduct_description()); // (8) description

            result = pstmt.executeUpdate();
            
        } finally {
            close();
        }
        return result;
    } // end of  public int productInsert(ProductDTO dto) throws SQLException ----
    
    // 전체 상품 목록 조회 (관리자용/사용자용 공통)
    public List<ProductDTO> selectProductAll(String productId, String userid) throws SQLException {
        List<ProductDTO> productList = new ArrayList<>();
        try {
            conn = ds.getConnection();
            // 최신 등록순으로 조회
            String sql = " SELECT product_id, product_name, pimage, sale_price, stock, " 
            		   + " (SELECT count(*) FROM tbl_wishlist WHERE product_id = P.product_id AND member_id = ?) AS is_wish "  
            		   + " FROM tbl_product ORDER BY product_id DESC ";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                ProductDTO pdto = new ProductDTO();
                pdto.setProduct_id(rs.getInt("product_id"));
                pdto.setProduct_name(rs.getString("product_name"));
                pdto.setPimage(rs.getString("pimage"));
                pdto.setSale_price(rs.getInt("sale_price"));
                pdto.setStock(rs.getInt("stock"));
                pdto.setIs_wish(rs.getInt("is_wish"));
                
                productList.add(pdto);
            }
        } finally { close(); }
        return productList;
    } // end of public List<ProductDTO> selectProductAll() throws SQLException ----
    

    // 페이징이 적용된 관리자용 전체 상품 리스트 조회
    @Override
    public List<ProductDTO> selectAllProductPaging(Map<String, String> paraMap) throws SQLException {
        List<ProductDTO> productList = new ArrayList<>();
        
        String category = paraMap.get("category");
        int startRno = Integer.parseInt(paraMap.get("startRno"));
        int endRno = Integer.parseInt(paraMap.get("endRno"));

        try {
            conn = ds.getConnection();
            
            String sql = " SELECT * "
                       + " FROM ( "
                       + "     SELECT rownum AS RNO, T.* "
                       + "     FROM ( "
                       + "         SELECT product_id, product_name, pimage, sale_price, stock, fk_category_id, pstatus "
                       + "         FROM tbl_product ";

            if(category != null && !category.trim().isEmpty()) {
                sql += "         WHERE pstatus = 1 AND fk_category_id = ? ";
            }

            sql += "         ORDER BY product_id DESC "
                 + "     ) T "
                 + " ) "
                 + " WHERE RNO BETWEEN ? AND ? ";

            pstmt = conn.prepareStatement(sql);
            
            int colIndex = 1;
            if(category != null && !category.trim().isEmpty()) {
                pstmt.setString(colIndex++, category);
            }
            pstmt.setInt(colIndex++, startRno);
            pstmt.setInt(colIndex++, endRno);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                ProductDTO pdto = new ProductDTO();
                pdto.setProduct_id(rs.getInt("product_id"));
                pdto.setProduct_name(rs.getString("product_name"));
                pdto.setFk_category_id(rs.getInt("fk_category_id"));
                pdto.setPimage(rs.getString("pimage"));
                pdto.setSale_price(rs.getInt("sale_price"));
                pdto.setStock(rs.getInt("stock"));
                pdto.setPstatus(rs.getInt("pstatus"));
                productList.add(pdto);
            }
        } finally {
            close();
        }
        return productList;
    }
    
    // 관리자전용 - 상품 수정
    @Override
    public int updateProduct(ProductDTO pdto) throws SQLException {
        int result = 0;
        try {
        	conn = ds.getConnection();
            
            String sql = " UPDATE tbl_product SET fk_category_id = ?, "
                       + " product_name = ?, sale_price = ?, stock = ?, "
                       + " product_description = ?, pimage = ? " // 추가됨
                       + " WHERE product_id = ? ";
         
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, pdto.getFk_category_id());
	        pstmt.setString(2, pdto.getProduct_name());
	        pstmt.setInt(3, pdto.getSale_price());
	        pstmt.setInt(4, pdto.getStock());
	        pstmt.setString(5, pdto.getProduct_description()); // 추가됨
	        pstmt.setString(6, pdto.getPimage());
	        pstmt.setInt(7, pdto.getProduct_id());
	         
	        result = pstmt.executeUpdate();
        } finally {
            close();
        }
        return result;
    } // end of public int updateProduct(ProductDTO pdto) throws SQLException ----
    
    // 관리자전용 - 상품 삭제
    public int deleteProduct(String productId) throws SQLException {
        int result = 0;
        try {
            conn = ds.getConnection();

            // 1. 주문내역(tbl_order_detail)과 리뷰(tbl_product_review) 존재 여부 합산 체크
            // 주의: tbl_order_detail 테이블 이름이 실제 DB와 일치하는지 꼭 확인하세요!
            String checkSql = " SELECT "
                            + " (SELECT count(*) FROM tbl_order_detail WHERE FK_PRODUCT_ID = ?) + "
                            + " (SELECT count(*) FROM tbl_product_review WHERE fk_product_id = ?) " 
                            + " AS total_count FROM dual ";
            
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, productId);
            pstmt.setString(2, productId);
            rs = pstmt.executeQuery();

            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt("total_count");
            }

            String sql = "";
            if (totalCount == 0) {
                // 자식 레코드가 하나도 없으면 진짜 삭제 (DELETE)
                sql = " DELETE FROM tbl_product WHERE product_id = ? ";
            } else {
                // 자식 레코드가 있으면 무결성 제약조건 때문에 삭제 대신 상태만 변경 (UPDATE)
                sql = " UPDATE tbl_product SET pstatus = 0 WHERE product_id = ? ";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, productId);
            result = pstmt.executeUpdate();

        } finally {
            close();
        }
        return result;
    } // end of  public int deleteProduct(String productId) throws SQLException ----

    // 찜하기
	@Override
	public int processWish(String userid, String product_id) {
	    int n = 0;
	    try {
	        conn = ds.getConnection();
	        
	        // 유저가 이 상품을 찜했는지 확인
	        String sql = " SELECT count(*) FROM tbl_wishlist "
	        		   + " WHERE member_id = ? AND product_id = ? ";
	        
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setString(1, userid);
	        pstmt.setString(2, product_id);
	        
	        rs = pstmt.executeQuery();
	        rs.next();
	        
	        int count = rs.getInt(1);

	        if(count == 0) {
	            // 찜 기록 없으면 추가(Insert)
	            sql = " INSERT INTO tbl_wishlist(wish_id, member_id, product_id, wish_date) "
	            	+ " VALUES(seq_wishlist_id.nextval, ?, ?, default) ";
	            
	            pstmt = conn.prepareStatement(sql);
	            
	            pstmt.setString(1, userid);
	            pstmt.setString(2, product_id);
	            
	            if(pstmt.executeUpdate() == 1) {
	            	n = 1; 
	            }
	        } else {
	            // 찜 기록 있으면 삭제(Delete)
	            sql = " DELETE FROM tbl_wishlist "
	            	+ " WHERE member_id = ? AND product_id = ? ";
	            
	            pstmt = conn.prepareStatement(sql);
	            
	            pstmt.setString(1, userid);
	            pstmt.setString(2, product_id);
	            
	            if(pstmt.executeUpdate() == 1) {
	            	n = -1; 
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        n = 0;
	    } finally {
	        close();
	    }
	    return n;
	}

	// 위시리스트 보기
	@Override
	public List<ProductDTO> getWishList(String userid) throws SQLException {
		
		List<ProductDTO> wishList = new ArrayList<>();
	    
	    try {
	        conn = ds.getConnection();
	        
	        // tbl_wish, tbl_product -> JOIN
	        String sql = " SELECT P.product_id, P.product_name, P.pimage, P.sale_price, P.stock, P.pstatus " +
	                     " FROM tbl_wishlist W JOIN tbl_product P " +
	                     " ON W.product_id = P.product_id " +
	                     " WHERE W.member_id = ? " +
	                     " ORDER BY W.wish_date DESC "; 
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            ProductDTO pdto = new ProductDTO();
	            pdto.setProduct_id(rs.getInt("product_id"));
	            pdto.setProduct_name(rs.getString("product_name"));
	            pdto.setPimage(rs.getString("pimage"));
	            pdto.setSale_price(rs.getInt("sale_price"));
	            pdto.setStock(rs.getInt("stock"));
	            pdto.setPstatus(rs.getInt("pstatus"));
	            wishList.add(pdto);
	        }
	        
	    } finally {
	        close();
	    }
	    
	    return wishList;
	}
	
	// 장바구니 추가
	@Override
	public int addCart(Map<String, String> paraMap) throws SQLException {
	    
		System.out.println("~~~ 확인용 userid : " + paraMap.get("userid"));
	    System.out.println("~~~ 확인용 product_id : " + paraMap.get("product_id"));
	    System.out.println("~~~ 확인용 cart_qty : " + paraMap.get("cart_qty"));
		
		
		int result = 0;
	    
	    try {
	        conn = ds.getConnection();
	        
	        // 사용자의 장바구니에 해당 상품이 이미 있는지 확인
	        String sql = " SELECT cart_id FROM tbl_cart " +
	                     " WHERE fk_member_id = ? AND fk_product_id = ? ";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, paraMap.get("userid"));
	        pstmt.setString(2, paraMap.get("product_id"));
	        
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
	            // 이미 있다면 수량만 더해주는 UPDATE 실행
	            int cart_id = rs.getInt("cart_id");
	            
	            sql = " UPDATE tbl_cart SET cart_qty = cart_qty + ? " +
	                  " WHERE cart_id = ? ";
	            
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, Integer.parseInt(paraMap.get("cart_qty")));
	            pstmt.setInt(2, cart_id);
	            
	            result = pstmt.executeUpdate();
	        } 
	        else {
	            // 없다면 새로 저장하는 INSERT 실행
	            sql = " INSERT INTO tbl_cart(cart_id, fk_member_id, fk_product_id, cart_qty) " +
	                  " VALUES(seq_cart_id.nextval, ?, ?, ?) "; 
	            
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, paraMap.get("userid"));
	            pstmt.setString(2, paraMap.get("product_id"));
	            pstmt.setInt(3, Integer.parseInt(paraMap.get("cart_qty")));
	            
	            result = pstmt.executeUpdate();
	        }
	        
	    } finally {
	        close();
	    }
	    
	    return result;
	} // end of public int addCart(Map<String, String> paraMap) throws SQLException ----
	
	// 장바구니 목록 조회
	@Override
	public List<CartDTO> getCartList(String userid) throws SQLException {
	    
	    List<CartDTO> cartList = new ArrayList<>();
	    
	    try {
	        conn = ds.getConnection();
	        
	        // 장바구니 정보와 상품 정보를 JOIN하여 가져옵니다.
	        String sql = " SELECT C.cart_id, C.fk_member_id, C.fk_product_id, C.cart_qty, " +
	                     "        P.product_name, P.pimage, P.sale_price, P.pstatus, P.stock " +
	                     " FROM tbl_cart C JOIN tbl_product P " +
	                     " ON C.fk_product_id = P.product_id " +
	                     " WHERE C.fk_member_id = ? " +
	                     " ORDER BY C.cart_date DESC ";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            CartDTO cdto = new CartDTO();
	            cdto.setCart_id(rs.getInt("cart_id"));
	            cdto.setCart_qty(rs.getInt("cart_qty"));
	            
	            // JOIN된 상품 정보를 ProductDTO에 담습니다.
	            ProductDTO pdto = new ProductDTO();
	            pdto.setProduct_id(rs.getInt("fk_product_id"));
	            pdto.setProduct_name(rs.getString("product_name"));
	            pdto.setPimage(rs.getString("pimage"));
	            pdto.setSale_price(rs.getInt("sale_price"));
	            pdto.setPstatus(rs.getInt("pstatus"));
	            pdto.setStock(rs.getInt("stock"));
	            
	            // CartDTO에 ProductDTO를 꽂아줍니다.
	            cdto.setPdto(pdto);
	            
	            cartList.add(cdto);
	        }
	        
	    } finally {
	        close();
	    }
	    
	    return cartList;
	} // end of public List<CartDTO> getCartList(String userid) throws SQLException ----
    
	// 장바구니 상품 수량 업데이트 //
	@Override
	public int updateCartQty(Map<String, String> paraMap) throws SQLException {
	    int result = 0;
	    try {
	        conn = ds.getConnection();

	        String sql = " UPDATE tbl_cart SET cart_qty = cart_qty + ? " +
	                     " WHERE cart_id = ? " +
	                     " AND (cart_qty + ?) >= 1 " +
	                     " AND (cart_qty + ?) <= (SELECT stock FROM tbl_product WHERE product_id = " +
	                     "                        (SELECT fk_product_id FROM tbl_cart WHERE cart_id = ?)) ";
	        
	        pstmt = conn.prepareStatement(sql);
	        int change = Integer.parseInt(paraMap.get("change"));
	        String cart_id = paraMap.get("cart_id");
	        
	        pstmt.setInt(1, change);    
	        pstmt.setString(2, cart_id); 
	        pstmt.setInt(3, change);    
	        pstmt.setInt(4, change);   
	        pstmt.setString(5, cart_id); 
	        
	        result = pstmt.executeUpdate();
	        
	    } finally {
	        close();
	    }
	    return result;
	}
	
	// 장바구니 상품 삭제
	@Override
	public int deleteCart(String cart_id) throws SQLException {
	    int result = 0;
	    try {
	        conn = ds.getConnection();
	        
	        String sql = " DELETE FROM tbl_cart WHERE cart_id = ? ";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, cart_id);
	        
	        result = pstmt.executeUpdate();
	        
	    } finally {
	        close();
	    }
	    return result;
	}
	

	
	// 주문하기
	@Override
	public int orderAdd(Map<String, Object> paraMap) throws SQLException {
	    int result = 0;

	    try {
	        conn = ds.getConnection();
	        conn.setAutoCommit(false);

	        String odrcode = "";
	        String sql_getOdrcode = " select seq_odrcode.nextval from dual ";
	        pstmt = conn.prepareStatement(sql_getOdrcode);
	        rs = pstmt.executeQuery();

	        if(rs.next()) {
	            odrcode = rs.getString(1);
	        }

	        paraMap.put("odrcode", odrcode);

	        String addrId = String.valueOf(paraMap.get("addr_id"));

	        // 배송지 1건 조회 (같은 conn 사용)
	        String postcode = "";
	        String address = "";
	        String detailaddress = "";
	        String extraaddress = "";

	        // 주소 조회
	        String sql_addr = " select postcode, address, detailaddress, extraaddress "
	                        + " from tbl_address "
	                        + " where addr_id = ? ";

	        pstmt = conn.prepareStatement(sql_addr);
	        pstmt.setString(1, addrId);
	        rs = pstmt.executeQuery();

	        if(rs.next()) {
	            postcode = rs.getString("postcode");
	            address = rs.getString("address");
	            detailaddress = rs.getString("detailaddress");
	            extraaddress = rs.getString("extraaddress");
	        }
	        else {
	            conn.rollback();
	            return 0;
	        }

	        // 주문 메인 테이블 인서트
	        String sql = " insert into tbl_order(odrcode, fk_member_id, fk_addr_id, odrtotalprice, odrtotalpoint, payment_status, "
	                   + " postcode, address, detailaddress, extraaddress) "
	                   + " values(?, ?, ?, ?, ?, 1, ?, ?, ?, ?) ";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, odrcode);
	        pstmt.setString(2, String.valueOf(paraMap.get("userid")));
	        pstmt.setString(3, addrId);
	        pstmt.setInt(4, Integer.parseInt(String.valueOf(paraMap.get("totalPrice"))));
	        pstmt.setInt(5, Integer.parseInt(String.valueOf(paraMap.get("totalPoint"))));
	        pstmt.setString(6, postcode);
	        pstmt.setString(7, address);
	        pstmt.setString(8, detailaddress);
	        pstmt.setString(9, extraaddress);

	        int n1 = pstmt.executeUpdate();

	        if(n1 != 1) {
	            conn.rollback();
	            return 0;
	        }

	        // 주문 상품 목록 구성 (단건, 장바구니)
	        String orderType = (String)paraMap.get("orderType");
	        List<Map<String, Object>> itemList = new ArrayList<>();

	        if("direct".equals(orderType)) {
	            Map<String, Object> directItem = new HashMap<>();
	            directItem.put("product_id", paraMap.get("product_id"));
	            directItem.put("qty", paraMap.get("qty"));
	            directItem.put("sale_price", paraMap.get("sale_price"));
	            itemList.add(directItem);
	        }
	        else {
	            String cartIds = String.valueOf(paraMap.get("cartIds"));
	            String sql_cart = " select c.fk_product_id, c.cart_qty, p.sale_price "
	                            + " from tbl_cart c join tbl_product p on c.fk_product_id = p.product_id "
	                            + " where c.cart_id in (" + cartIds + ") ";

	            try (PreparedStatement pstmt2 = conn.prepareStatement(sql_cart);
	                 ResultSet rs2 = pstmt2.executeQuery()) {
	                while(rs2.next()) {
	                    Map<String, Object> map = new HashMap<>();
	                    map.put("product_id", rs2.getString("fk_product_id"));
	                    map.put("qty", rs2.getInt("cart_qty"));
	                    map.put("sale_price", rs2.getInt("sale_price"));
	                    itemList.add(map);
	                }
	            }
	        }

	        // 주문 상세 인서트 및 재고 차감
	        int n2_total = 1;
	        int n3_total = 1;

	        for(Map<String, Object> item : itemList) {
	            sql = " insert into tbl_order_detail(odrdetailno, fk_odrcode, fk_product_id, odrqty, odrprice) "
	                + " values(seq_odrdetailno.nextval, ?, ?, ?, ?) ";

	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, odrcode);
	            pstmt.setString(2, String.valueOf(item.get("product_id")));
	            pstmt.setInt(3, Integer.parseInt(String.valueOf(item.get("qty"))));
	            pstmt.setInt(4, Integer.parseInt(String.valueOf(item.get("sale_price"))));

	            if(pstmt.executeUpdate() != 1) { 
	                n2_total = 0; 
	                break; 
	            }

	            sql = " update tbl_product set stock = stock - ? "
	                + " where product_id = ? and stock >= ? ";

	            pstmt = conn.prepareStatement(sql);
	            int qty = Integer.parseInt(String.valueOf(item.get("qty")));
	            pstmt.setInt(1, qty);
	            pstmt.setString(2, String.valueOf(item.get("product_id")));
	            pstmt.setInt(3, qty);

	            if(pstmt.executeUpdate() != 1) { 
	                n3_total = 0; 
	                break; 
	            }
	        }

	        // 장바구니 비우기
	        int n4 = 1;
	        if("cart".equals(orderType)) {
	            String cartIds = String.valueOf(paraMap.get("cartIds"));
	            sql = " delete from tbl_cart where cart_id in (" + cartIds + ") ";
	            pstmt = conn.prepareStatement(sql);
	            n4 = pstmt.executeUpdate();
	        }

	        // 포인트 처리
	        int n5_1 = 1;
	        int n5_2 = 1;

	        int usePoint = Integer.parseInt(String.valueOf(paraMap.get("usePoint")));
	        if(usePoint > 0) {
	            sql = " update tbl_member set point = point - ? where member_id = ? ";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, usePoint);
	            pstmt.setString(2, String.valueOf(paraMap.get("userid")));
	            n5_1 = pstmt.executeUpdate();
	        }

	        int totalPoint = Integer.parseInt(String.valueOf(paraMap.get("totalPoint")));
	        if(totalPoint > 0) {
	            sql = " update tbl_member set point = point + ? where member_id = ? ";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, totalPoint);
	            pstmt.setString(2, String.valueOf(paraMap.get("userid")));
	            n5_2 = pstmt.executeUpdate();
	        }

	        if(n1 == 1 && n2_total == 1 && n3_total == 1 && n4 > 0 && n5_1 == 1 && n5_2 == 1) {
	            conn.commit();
	            result = 1;
	        }
	        else {
	            conn.rollback();
	            result = 0;
	        }

	    } catch (Exception e) {
	        if(conn != null) conn.rollback();
	        e.printStackTrace();
	        result = 0;
	    } finally {
	        if(conn != null) conn.setAutoCommit(true);
	        close();
	    }

	    return result;
	}

	// 주문 상품 정보 불러오기 -> 상품 상세페이지, 바로 구매시
	@Override
	public ProductDTO getProductDetail(String productId) throws SQLException {
	    ProductDTO pdto = null;
	    try {
	        conn = ds.getConnection();
	        
	        String sql = " select product_id, product_name, sale_price, list_price, stock, pimage, product_description, fk_category_id "
	                   + " from tbl_product "
	                   + " where product_id = ? "; 
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, productId);
	        
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
	            pdto = new ProductDTO();
	            pdto.setProduct_id(rs.getInt("product_id"));
	            pdto.setProduct_name(rs.getString("product_name"));
	            pdto.setSale_price(rs.getInt("sale_price"));
	            pdto.setList_price(rs.getInt("list_price"));
	            pdto.setStock(rs.getInt("stock"));
	            pdto.setPimage(rs.getString("pimage"));
	            pdto.setProduct_description(rs.getString("product_description"));
	            pdto.setFk_category_id(rs.getInt("fk_category_id"));
	        }
	    } finally {
	        close();
	    }
	    return pdto; 
	}

	// 장바구니에서 여러 상품 주문
	@Override
	public List<Map<String, Object>> getCartListByCartIds(String cartIds) throws SQLException {
	    List<Map<String, Object>> orderList = new ArrayList<>();
	    try {
	        conn = ds.getConnection();
	        String sql = " select c.cart_id, c.fk_product_id, c.cart_qty, "
	                   + "        p.product_name, p.pimage, p.sale_price "
	                   + " from tbl_cart c join tbl_product p "
	                   + " on c.fk_product_id = p.product_id "
	                   + " where c.cart_id in (" + cartIds + ") ";
	        
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("product_id", rs.getString("fk_product_id")); // 상세 테이블용
	            map.put("cart_qty", rs.getInt("cart_qty"));           // 주문 수량
	            map.put("sale_price", rs.getInt("sale_price"));       // 실제 DB 가격
	            map.put("product_name", rs.getString("product_name")); 
	            map.put("pimage", rs.getString("pimage"));
	            orderList.add(map);
	        }
	    } finally {
	        close();
	    }
	    return orderList;
	}
	
	// 주문자 주소 가져오기
	// 주문자 주소 목록 가져오기
	@Override
	public List<Map<String, String>> getAddressList(String userid) throws SQLException {
	    
	    List<Map<String, String>> addrList = new ArrayList<>();
	    
	    try {
	        conn = ds.getConnection();
	        
	        String sql = " select addr_id, postcode, address, detailaddress, extraaddress "
	                   + " from tbl_address "
	                   + " where fk_member_id = ? and status = 1 "
	                   + " order by addr_id desc "; 
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            Map<String, String> map = new HashMap<>();
	            map.put("addr_id", rs.getString("addr_id"));
	            map.put("postcode", rs.getString("postcode"));
	            map.put("address", rs.getString("address"));
	            map.put("detailaddress", rs.getString("detailaddress"));
	            map.put("extraaddress", rs.getString("extraaddress"));
	            
	            addrList.add(map);
	        }
	        
	    } finally {
	        close();
	    }
	    
	    return addrList;
	}
	
	// 주소 등록하기
	@Override
	public int registerAddress(Map<String, String> paraMap) throws SQLException {
	    int n = 0;
	    try {
	        conn = ds.getConnection();
	        
	        String sql = " insert into tbl_address(addr_id, fk_member_id, postcode, address, detailaddress, extraaddress) "
	                   + " values(seq_addr_id.nextval, ?, ?, ?, ?, ?) ";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, paraMap.get("userid"));
	        pstmt.setString(2, paraMap.get("postcode"));
	        pstmt.setString(3, paraMap.get("address"));
	        pstmt.setString(4, paraMap.get("detailaddress"));
	        pstmt.setString(5, paraMap.get("extraaddress"));
	        
	        n = pstmt.executeUpdate();
	    } finally {
	        close();
	    }
	    return n;
	}

	// 주문 완료 후 장바구니 비우기
	@Override
	public int deleteCartList(String cartIds) throws SQLException {
	    int n = 0;
	    try {
	        conn = ds.getConnection();
	        // 주문에 사용된 장바구니 항목들만 삭제
	        String sql = " delete from tbl_cart where cart_id in (" + cartIds + ") ";
	        
	        pstmt = conn.prepareStatement(sql);
	        n = pstmt.executeUpdate();
	    } finally {
	        close();
	    }
	    return n;
	}
	
	// 페이징 처리를 한 카테고리별 상품 조회
	@Override
	public List<ProductDTO> selectProductByCategoryPaging(Map<String, String> paraMap) throws SQLException {
	    
	    List<ProductDTO> productList = new ArrayList<>();
	    
	    try {
	        conn = ds.getConnection();
	        
	        // Oracle 페이징의 핵심: 3중 서브쿼리 (정렬 -> 번호매기기 -> 범위자르기)
	        String sql = " SELECT * "
	                   + " FROM ( "
	                   + "     SELECT rownum AS RNO, T.* "
	                   + "     FROM ( "
	                   + "         SELECT product_id, product_name, pimage, sale_price, list_price, stock, fk_category_id, pstatus, "
	                   + "                (SELECT count(*) FROM tbl_wishlist WHERE product_id = P.product_id AND member_id = ?) AS is_wish "
	                   + "         FROM tbl_product P "
	                   + "         WHERE pstatus = 1 AND fk_category_id = ? "
	                   + "         ORDER BY product_id DESC "
	                   + "     ) T "
	                   + " ) "
	                   + " WHERE RNO BETWEEN ? AND ? ";
	        
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setString(1, paraMap.get("userid")); 
	        pstmt.setInt(2, Integer.parseInt(paraMap.get("categoryId"))); 
	        pstmt.setInt(3, Integer.parseInt(paraMap.get("startRno"))); // 시작 행 번호
	        pstmt.setInt(4, Integer.parseInt(paraMap.get("endRno")));   // 끝 행 번호
	        
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            ProductDTO pdto = new ProductDTO();
	            pdto.setProduct_id(rs.getInt("product_id"));
	            pdto.setProduct_name(rs.getString("product_name"));
	            pdto.setPimage(rs.getString("pimage"));
	            pdto.setSale_price(rs.getInt("sale_price"));
	            pdto.setList_price(rs.getInt("list_price"));
	            pdto.setStock(rs.getInt("stock"));
	            pdto.setFk_category_id(rs.getInt("fk_category_id"));
	            pdto.setIs_wish(rs.getInt("is_wish"));
	            pdto.setPstatus(rs.getInt("pstatus"));
	            productList.add(pdto);
	        }
	    } finally {
	        close();
	    }
	    return productList;
	}

	// 페이지바를 만들기 위한 해당 카테고리의 총 상품 개수 조회
	@Override
	public int getTotalCountByCategory(String categoryId) throws SQLException {
	    int totalCount = 0;
	    try {
	        conn = ds.getConnection();
	        String sql = " SELECT count(*) FROM tbl_product WHERE fk_category_id = ? ";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, categoryId);
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	            totalCount = rs.getInt(1);
	        }
	    } finally {
	        close();
	    }
	    return totalCount;
	}
	
	
	// 페이지바 생성
	public String getPageBar(int currentShowPageNo, int sizePerPage, int totalCount, String categoryId) {
	    
	    String pageBar = "";
	    
	    int blockSize = 5;    // 한 블럭당 보여줄 페이지 번호 개수 (예: [1] [2] [3] [4] [5])
	    int loop = 1;         // blockSize만큼 반복하기 위한 변수
	    
	    // 전체 페이지 수 계산
	    int totalPage = (int) Math.ceil((double) totalCount / sizePerPage);
	    
	    // 시작 페이지 번호 계산 (현재 페이지가 3이면 1, 현재 페이지가 7이면 6)
	    int pageNo = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;

	    // [이전] 버튼
	    if (pageNo != 1) {
	        pageBar += "<li class='page-item'>" +
	                   "  <a class='page-link' href='productList.sp?category=" + categoryId + "&currentShowPageNo=" + (pageNo - 1) + "' aria-label='Previous'>" +
	                   "    <span aria-hidden='true'>&laquo;</span>" +
	                   "  </a>" +
	                   "</li>";
	    }

	    // 페이지 번호 생성 (반복문)
	    while (!(loop > blockSize || pageNo > totalPage)) {
	        if (pageNo == currentShowPageNo) {
	            // 현재 내가 보고 있는 페이지 (클릭 안되게 active 처리)
	            pageBar += "<li class='page-item active'><span class='page-link'>" + pageNo + "</span></li>";
	        } else {
	            // 다른 페이지 번호
	            pageBar += "<li class='page-item'><a class='page-link' href='productList.sp?category=" + categoryId + "&currentShowPageNo=" + pageNo + "'>" + pageNo + "</a></li>";
	        }
	        pageNo++;
	        loop++;
	    }

	    // [다음] 버튼 
	    if (pageNo <= totalPage) {
	        pageBar += "<li class='page-item'>" +
	                   "  <a class='page-link' href='productList.sp?category=" + categoryId + "&currentShowPageNo=" + pageNo + "' aria-label='Next'>" +
	                   "    <span aria-hidden='true'>&raquo;</span>" +
	                   "  </a>" +
	                   "</li>";
	    }

	    return pageBar;
	}
	
	// 페이지바를 만들기 위한 해당 카테고리의 총 상품 개수
	@Override
	public int getTotalProductCount(String category) throws SQLException {
	    int totalCount = 0;
	    try {
	        conn = ds.getConnection();
	        String sql = " SELECT count(*) FROM tbl_product ";
	        
	        if(category != null && !category.trim().isEmpty()) {
	            sql += " WHERE fk_category_id = ? ";
	        }
	        
	        pstmt = conn.prepareStatement(sql);
	        
	        if(category != null && !category.trim().isEmpty()) {
	            pstmt.setString(1, category);
	        }
	        
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	            totalCount = rs.getInt(1);
	        }
	    } finally {
	        close();
	    }
	    return totalCount;
	}
	
	// 관리자 전용 페이지바 생성 
	@Override
	public String getAdminPageBar(int currentShowPageNo, int sizePerPage, int totalCount, String category) {
	    String pageBar = "";
	    int blockSize = 5;   
	    int loop = 1;         
	    int totalPage = (int) Math.ceil((double) totalCount / sizePerPage);
	    int pageNo = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;

	    String catParam = (category == null) ? "" : category;

	    // [이전]
	    if (pageNo != 1) {
	        pageBar += "<li class='page-item'>" +
	                   "  <a class='page-link' href='allproductList.sp?category=" + catParam + "&currentShowPageNo=" + (pageNo - 1) + "'>&laquo;</a>" +
	                   "</li>";
	    }

	    // 페이지 번호
	    while (!(loop > blockSize || pageNo > totalPage)) {
	        if (pageNo == currentShowPageNo) {
	            pageBar += "<li class='page-item active'><span class='page-link'>" + pageNo + "</span></li>";
	        } else {
	            pageBar += "<li class='page-item'><a class='page-link' href='allproductList.sp?category=" + catParam + "&currentShowPageNo=" + pageNo + "'>" + pageNo + "</a></li>";
	        }
	        pageNo++;
	        loop++;
	    }

	    // [다음]
	    if (pageNo <= totalPage) {
	        pageBar += "<li class='page-item'>" +
	                   "  <a class='page-link' href='allproductList.sp?category=" + catParam + "&currentShowPageNo=" + pageNo + "'>&raquo;</a>" +
	                   "</li>";
	    }

	    return pageBar;
	}

	// 장바구니 번호로 상품의 상태와 정보를 조회 (주문 전 검증용)
	@Override
	public ProductDTO getProductByCartId(String cart_id) throws SQLException {
		
		ProductDTO pdto = null;
	    
	    try {
	        conn = ds.getConnection();
	        
	        // 장바구니(C)와 상품(P) 테이블을 조인하여 상품명과 판매상태를 가져옴
	        String sql = " SELECT P.product_name, P.pstatus, P.product_id "
	                   + " FROM tbl_cart C JOIN tbl_product P "
	                   + " ON C.fk_product_id = P.product_id "
	                   + " WHERE C.cart_id = ? ";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, cart_id);
	        
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
	            pdto = new ProductDTO();
	            pdto.setProduct_name(rs.getString("product_name"));
	            pdto.setPstatus(rs.getInt("pstatus"));
	            pdto.setProduct_id(rs.getInt("product_id"));
	        }
	        
	    } finally {
	        close();
	    }
	    
	    return pdto;
	}

	// DB 업데이트 (배송상태를 4:주문취소 로 변경하는 로직)
	@Override
	public int updateOrderCancel(String odrcode) throws SQLException {
		
		int result = 0;
	    
	    try {
	        conn = ds.getConnection();
	        
	        conn.setAutoCommit(false);

	        // 1. 주문 상세 테이블(tbl_order_detail)의 모든 상품 상태를 '4(주문취소)'로 변경
	        String sql = " UPDATE tbl_order_detail SET deliverystatus = 4 "
	                   + " WHERE fk_odrcode = ? ";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, odrcode);
	        
	        int n1 = pstmt.executeUpdate();

	        if(n1 > 0) {
	            conn.commit();
	            conn.setAutoCommit(true);
	            result = 1;
	        } else {
	            conn.rollback();
	        }

	    } catch (SQLException e) {
	        if(conn != null) conn.rollback(); 
	        throw e;
	    } finally {
	        close();
	    }
	    
	    return result;
	}

	
}