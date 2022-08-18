package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class JdbcMemberRepository implements MemberRepository {
    private final DataSource dataSource;
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public Member save(Member member) {

        String sql = "insert into member(name) values(?)";  //1 사용할 sql 쿼리문 정의

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;  //2 결과를 받는 객체

        try {
            conn = getConnection();  //3 데이터베이스 커넥션 가져오기
            pstmt = conn.prepareStatement(sql,  //4 상단에 정의해놓은 sql문 가져오기
                    Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, member.getName());   //5 상당 sql문의 ?와 매칭되는 paramIndex 1, member.getName으로 값 삽입

            pstmt.executeUpdate();   //6 DB에 실제 쿼리 날아감
            rs = pstmt.getGeneratedKeys();  //7 상단의 RETURN_GENERATED_KEYS와 매칭되어 DB가 생성한 Key를 반환

            if (rs.next()) {                          //8 rs에 객체가 있으면
                member.setId(rs.getLong(1));  //9 rs에 넣어놓은 id값 꺼내서 setId로 저장
            } else {
                throw new SQLException("id 조회 실패");  //10 rs내에 아무것도 없으면 예외 던지기
            }
            return member;

        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            close(conn, pstmt, rs);  //11 DB커넥션은 사용 종료 즉시 연결 해제 해야함 -> 아니면 커넥션 계속 쌓임
        }
    }
    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);

            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();

            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    //복잡한 close 메소드....
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}