package com.zipnivasa.backend.modules.profile.service;

import com.zipnivasa.backend.modules.profile.dto.ProfileUpdateRequest;
import com.zipnivasa.backend.modules.user.model.MessOwner;
import com.zipnivasa.backend.modules.user.model.PGOwner;
import com.zipnivasa.backend.modules.user.model.Tenant;
import com.zipnivasa.backend.modules.user.model.User;
import com.zipnivasa.backend.modules.user.repository.MessOwnerRepository;
import com.zipnivasa.backend.modules.user.repository.PGOwnerRepository;
import com.zipnivasa.backend.modules.user.repository.TenantRepository;
import com.zipnivasa.backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PGOwnerRepository pgOwnerRepository;
    private final MessOwnerRepository messOwnerRepository;


    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }

    public Object getRoleData(String role, String userId) {
        return switch (role) {
            case "tenant" -> tenantRepository.findByUserId(userId).orElse(null);
            case "pgowner" -> pgOwnerRepository.findByUserId(userId).orElse(null);
            case "messowner" -> messOwnerRepository.findByUserId(userId).orElse(null);
            default -> null;
        };
    }


    public void updateProfile(String userId, String role, ProfileUpdateRequest req) {
        userRepository.findById(userId).ifPresent(user -> {
            if (req.getName() != null) user.setName(req.getName());
            if (req.getPhone() != null) user.setPhone(req.getPhone());
            userRepository.save(user);
        });

        Map<String, Object> roleData = req.getRoleData();
        if (roleData == null || roleData.isEmpty()) return;

        switch (role) {
            case "tenant" -> tenantRepository.findByUserId(userId)
                    .ifPresent(t -> {
                        applyMapToObject(roleData, t);
                        tenantRepository.save(t);
                    });
            case "pgowner" -> pgOwnerRepository.findByUserId(userId)
                    .ifPresent(p -> {
                        applyMapToObject(roleData, p);
                        pgOwnerRepository.save(p);
                    });
            case "messowner" -> messOwnerRepository.findByUserId(userId)
                    .ifPresent(m -> {
                        applyMapToObject(roleData, m);
                        messOwnerRepository.save(m);
                    });
            default -> {
            }
        }
    }

    private void applyMapToObject(Map<String, Object> map, Object target) {
        Class<?> clazz = target.getClass();
        map.forEach((key, value) -> {
            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);

                Object converted = convertValue(field.getType(), value);
                field.set(target, converted);
            } catch (NoSuchFieldException ignored) {
            } catch (IllegalAccessException e) {
            }
        });
    }

    private Object convertValue(Class<?> targetType, Object value) {
        if (value == null) return null;

        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (targetType == Integer.class || targetType == int.class) {
            return Integer.valueOf(value.toString());
        }
        if (targetType == Long.class || targetType == long.class) {
            return Long.valueOf(value.toString());
        }
        if (targetType == Double.class || targetType == double.class) {
            return Double.valueOf(value.toString());
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.valueOf(value.toString());
        }

        return value.toString();
    }
}
