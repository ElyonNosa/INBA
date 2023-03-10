package comp3350.inba.persistence;

import java.util.List;

import comp3350.inba.objects.User;

public interface UserPersistence {
    List<User> get_user_list();

    User insert_user(User usr);

    User update_user(User usr);

    void delete_user(User usr);
}
