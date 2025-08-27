const User = require('../models/User');
const { addJobToQueue, QueueStateManager } = require('./queueService');
const { logger } = require('../config/database');

class UserService {
  
  // Create a new user
  static async createUser(call, callback) {
    try {
      const { name, email, password, phone, role } = call.request;
      
      // Validate required fields
      if (!name || !email || !password || !phone) {
        return callback(null, {
          success: false,
          message: 'Name, email, password, and phone are required',
          user: null
        });
      }

      // Check if user already exists
      const existingUser = await User.findOne({ email });
      if (existingUser) {
        return callback(null, {
          success: false,
          message: 'User with this email already exists',
          user: null
        });
      }

      // Create user
      const userData = { name, email, password, phone, role: role || 'user' };
      const user = new User(userData);
      const savedUser = await user.save();

      // Add to queue for state management
      try {
        await addJobToQueue('create_user', {
          userId: savedUser._id.toString(),
          ...userData
        }, 'CREATE');
      } catch (queueError) {
        logger.warn('Failed to add create operation to queue:', queueError);
      }

      logger.info(`User created successfully: ${savedUser._id}`);

      callback(null, {
        success: true,
        message: 'User created successfully',
        user: {
          id: savedUser._id.toString(),
          name: savedUser.name,
          email: savedUser.email,
          phone: savedUser.phone,
          role: savedUser.role,
          created_at: savedUser.createdAt.toISOString(),
          updated_at: savedUser.updatedAt.toISOString()
        }
      });

    } catch (error) {
      logger.error('Error creating user:', error);
      callback(null, {
        success: false,
        message: 'Internal server error: ' + error.message,
        user: null
      });
    }
  }

  // Get user by ID
  static async getUser(call, callback) {
    try {
      const { id } = call.request;

      if (!id) {
        return callback(null, {
          success: false,
          message: 'User ID is required',
          user: null
        });
      }

      const user = await User.findById(id);
      if (!user) {
        return callback(null, {
          success: false,
          message: 'User not found',
          user: null
        });
      }

      // Skip queue for read operations to improve performance
      // Note: GET operations don't need queue tracking for state management

      logger.info(`User retrieved: ${user._id}`);

      callback(null, {
        success: true,
        message: 'User found',
        user: {
          id: user._id.toString(),
          name: user.name,
          email: user.email,
          phone: user.phone,
          role: user.role,
          created_at: user.createdAt.toISOString(),
          updated_at: user.updatedAt.toISOString()
        }
      });

    } catch (error) {
      logger.error('Error getting user:', error);
      callback(null, {
        success: false,
        message: 'Internal server error: ' + error.message,
        user: null
      });
    }
  }

  // Update user
  static async updateUser(call, callback) {
    try {
      const { id, name, email, phone, role } = call.request;

      if (!id) {
        return callback(null, {
          success: false,
          message: 'User ID is required',
          user: null
        });
      }

      // Check if user exists
      const existingUser = await User.findById(id);
      if (!existingUser) {
        return callback(null, {
          success: false,
          message: 'User not found',
          user: null
        });
      }

      // Check if email is being changed and if it's already taken
      if (email && email !== existingUser.email) {
        const emailExists = await User.findOne({ email, _id: { $ne: id } });
        if (emailExists) {
          return callback(null, {
            success: false,
            message: 'Email is already taken by another user',
            user: null
          });
        }
      }

      // Update user
      const updateData = {};
      if (name) updateData.name = name;
      if (email) updateData.email = email;
      if (phone) updateData.phone = phone;
      if (role) updateData.role = role;

      const updatedUser = await User.findByIdAndUpdate(
        id,
        updateData,
        { new: true, runValidators: true }
      );

      // Add to queue for state management
      try {
        await addJobToQueue('update_user', {
          userId: id,
          updateData
        }, 'UPDATE');
      } catch (queueError) {
        logger.warn('Failed to add update operation to queue:', queueError);
      }

      logger.info(`User updated successfully: ${updatedUser._id}`);

      callback(null, {
        success: true,
        message: 'User updated successfully',
        user: {
          id: updatedUser._id.toString(),
          name: updatedUser.name,
          email: updatedUser.email,
          phone: updatedUser.phone,
          role: updatedUser.role,
          created_at: updatedUser.createdAt.toISOString(),
          updated_at: updatedUser.updatedAt.toISOString()
        }
      });

    } catch (error) {
      logger.error('Error updating user:', error);
      callback(null, {
        success: false,
        message: 'Internal server error: ' + error.message,
        user: null
      });
    }
  }

  // Delete user
  static async deleteUser(call, callback) {
    try {
      const { id } = call.request;

      if (!id) {
        return callback(null, {
          success: false,
          message: 'User ID is required'
        });
      }

      const user = await User.findById(id);
      if (!user) {
        return callback(null, {
          success: false,
          message: 'User not found'
        });
      }

      await User.findByIdAndDelete(id);

      // Add to queue for state management
      try {
        await addJobToQueue('delete_user', {
          userId: id,
          deletedUserData: {
            name: user.name,
            email: user.email
          }
        }, 'DELETE');
      } catch (queueError) {
        logger.warn('Failed to add delete operation to queue:', queueError);
      }

      logger.info(`User deleted successfully: ${id}`);

      callback(null, {
        success: true,
        message: 'User deleted successfully'
      });

    } catch (error) {
      logger.error('Error deleting user:', error);
      callback(null, {
        success: false,
        message: 'Internal server error: ' + error.message
      });
    }
  }

  // List users with pagination and search
  static async listUsers(call, callback) {
    const startTime = Date.now(); // Track query performance
    try {
      const { page = 1, limit = 10, search = '' } = call.request;
      
      const pageNum = Math.max(1, parseInt(page));
      const limitNum = Math.min(100, Math.max(1, parseInt(limit)));
      const skip = (pageNum - 1) * limitNum;

      // Build optimized search query
      let query = {};
      if (search && search.trim()) {
        const searchTerm = search.trim();
        // Use text search for better performance when available
        if (searchTerm.includes('@')) {
          // If search contains @, prioritize email search
          query = { email: { $regex: searchTerm, $options: 'i' } };
        } else {
          // For other searches, use text search if possible, fallback to regex
          query = {
            $or: [
              { name: { $regex: searchTerm, $options: 'i' } },
              { email: { $regex: searchTerm, $options: 'i' } },
              { role: { $regex: searchTerm, $options: 'i' } }
            ]
          };
        }
      }

      // Optimize database queries with lean() for better performance
      const [users, total] = await Promise.all([
        User.find(query)
          .select('-password')
          .sort({ createdAt: -1 })
          .skip(skip)
          .limit(limitNum)
          .lean(), // Use lean() for better performance
        User.countDocuments(query)
      ]);

      // Skip queue for list operations to improve performance
      // Note: LIST operations don't need queue tracking for better performance

      const userList = users.map(user => ({
        id: user._id.toString(),
        name: user.name,
        email: user.email,
        phone: user.phone,
        role: user.role,
        created_at: user.createdAt.toISOString(),
        updated_at: user.updatedAt.toISOString()
      }));

      logger.info(`Listed users: page ${pageNum}, total ${total}, query time: ${Date.now() - startTime}ms`);

      callback(null, {
        success: true,
        message: `Found ${total} users`,
        users: userList,
        total: total,
        page: pageNum,
        limit: limitNum
      });

    } catch (error) {
      logger.error('Error listing users:', error);
      callback(null, {
        success: false,
        message: 'Internal server error: ' + error.message,
        users: [],
        total: 0,
        page: 1,
        limit: 10
      });
    }
  }
}

module.exports = UserService;
