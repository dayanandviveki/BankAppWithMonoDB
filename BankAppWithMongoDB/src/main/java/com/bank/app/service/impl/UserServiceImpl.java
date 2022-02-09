package com.bank.app.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import com.bank.app.exception.SendingFailedException;
import com.bank.app.exception.ValidationFailedException;
//import com.bank.app.model.Account;
import com.bank.app.model.User;
//import com.bank.app.repository.AccountRepository;
import com.bank.app.repository.UserRepository;
import com.bank.app.service.IUserService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    static Integer otp;

    //In this method send the OTP to the user mail id
    public void sendOtp( User user){

        Email from = new Email("dayanandviveki@gmail.com");
        String subject = "Email OTP Verification";
        Email to = new Email(user.getUserEmail());
        Request request = new Request();
        otp = new Random().nextInt(90000) + 10000;

        Content content = new Content("text/plain", "Hello "+user.getFirstName()+" "+ user.getLastName()+" "+otp+" is your one time password and "
                + "is valid for next 15 minutes. Please enter the same to complete your Email validation. Regards NoManey Bank.");

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw new SendingFailedException("Failed to send OTP");
        }
    }

    //In this methoad checking in the database Dublicate values are  there or not if there then throw validationexception
    public void checkDublicateDetails(User user) {

        List<User> listUser = getAllUser();
        for(User userCheck : listUser){
            if(userCheck.getUserPan().equalsIgnoreCase(user.getUserPan()))
                throw new ValidationFailedException("User Pan Card Already Used !!! Enter New Pan Card Details...");
            else if(userCheck.getUserEmail().equalsIgnoreCase(user.getUserEmail()))
                throw new ValidationFailedException("User Email Id Already Used !!! Enter New Email ID Details...");
            else if(userCheck.getMobileNo().equalsIgnoreCase(user.getMobileNo()))
                throw new ValidationFailedException("User Mobile Number Already Used !!! Enter New Mobile Number Details...");
            else if(userCheck.getUserUid().equalsIgnoreCase(user.getUserUid()))
                throw new ValidationFailedException("User Aadhar Number Already Used !!! Enter New Aadhar Number Details...");
        }
    }

    //In this method we are adding user data in database after all validation done.
    @Override
    public User createUser(User user){
        User newUser = null;
        checkDublicateDetails(user);
        try{
            sendOtp(user);
            String usernName = user.getFirstName()+user.getLastName()+new Random().nextInt(9999);
            user.setUserName(usernName);
            user.setIsActive(false);
            user.setOtp(String.valueOf(otp));
            newUser =  userRepository.save(user);
        }catch(NullPointerException npe){
            throw new ValidationFailedException("Invalid User Details !!!");
        }catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Username !!!");
        }
        return  newUser;
    }

    //In this method  update the specific or all user details by the username
    @Override
    public User updateUser(String userName, User user) {
        User oldUser= null;

        oldUser = getUserByUsername(userName);
        checkDublicateDetails(user);
        try{
            if(user.getFirstName() != null)
                oldUser.setFirstName(user.getFirstName());
            if(user.getMiddleName() != null)
                oldUser.setMiddleName(user.getMiddleName());
            if(user.getLastName() != null)
                oldUser.setLastName(user.getLastName());
            if(user.getFirstName() != null)
                oldUser.setFirstName(user.getFirstName());
            if(user.getUserEmail() != null)
                oldUser.setUserEmail(user.getUserEmail());
            if(user.getUserPan() != null)
                oldUser.setUserPan(user.getUserPan());
            if(user.getDateOfBirth() != null)
                oldUser.setDateOfBirth(user.getDateOfBirth());
            if(user.getAddress() != null)
                oldUser.setAddress(user.getAddress());
            if(user.getMobileNo() != null)
                oldUser.setMobileNo(user.getMobileNo());
            if(user.getUserUid() != null)
                oldUser.setUserUid(user.getUserUid());
            if(user.getPassword() != null)
                oldUser.setPassword(user.getPassword());

            sendOtp(oldUser);
            oldUser.setIsActive(false);
            oldUser.setOtp(String.valueOf(otp));
            oldUser = userRepository.save(oldUser);
        }catch(NullPointerException npe){
            throw new ValidationFailedException("Invalid User Details !!!");
        }catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Username !!!");
        }catch(TransactionSystemException tse){
            throw new ValidationFailedException("Database getting Error !!!");
        }catch(HttpMessageNotReadableException hmnqe){
            throw new ValidationFailedException("Invalid User Details !!!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return oldUser;
    }

    // In this method validation done by user with otp
    @Override
    public User validateUserByEmail(String otp, String userName) {

        User user = null;
       
        user = getUserByUsername(userName);
        if (!user.getIsActive()) {
            if (user.getOtp().equals(otp)) {
                user.setOtp(null);
                user.setIsActive(true);
                user = userRepository.save(user);
            } else
                throw new ValidationFailedException("Invalid OTP !!!");
        } else
            throw new ValidationFailedException("Otp Validation Already Done By User ...");
       
        return user;
    }

    //This method was sending OTP again to user if account is Inactive
    @Override
    public User resendOtp(String userName)  {
        User user= null ;

        user = getUserByUsername(userName);

        if (!user.getIsActive()){
            sendOtp(user);
            user.setOtp(String.valueOf(otp));
            user =  userRepository.save(user);
        }
        else
            throw new ValidationFailedException("OTP Validation Already Done By user ...");

        return user;
    }

    //this method returns all the User avalible in the database
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

       //this methoad return a user by username
    @Override
    public User getUserByUsername(String userName) {
        User user = null;
        try{
            user = userRepository.findById(userName).get();
        }catch(NoSuchElementException nsee){
            throw new ValidationFailedException("Invalid Username !!!");
        }
        return user;
    }
    
}