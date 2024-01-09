package chinookMgr;

import chinookMgr.db.HibernateUtil;
import chinookMgr.db.entities.EmployeeEntity;
import chinookMgr.db.entities.GenreEntity;
import chinookMgr.frontend.MainMenu;

import static chinookMgr.db.HibernateUtil.getSession;

public class Main {
	public static void main(String[] args) {
//		new MainMenu().setVisible(true);
		try (var s = getSession()) {
			s.createQuery("from EmployeeEntity ", EmployeeEntity.class).list().forEach(g -> System.out.println(g.getFirstName()));
		}
	}
}