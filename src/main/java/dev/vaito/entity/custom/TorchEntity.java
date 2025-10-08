package dev.vaito.entity.custom;

import dev.vaito.entity.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TorchEntity extends ThrownItemEntity {
    /*
     * Da default things
     * */
    public TorchEntity(EntityType<? extends TorchEntity> entityType, World world) {
        super(entityType, world);
    }

    public TorchEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.TORCH, owner, world, stack);
    }

    public TorchEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntities.TORCH, x, y, z, world, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TORCH;
    }

    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getStack();
        return (ParticleEffect) (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for (int i = 0; i < 8; i++) {
                this.getWorld().addParticleClient(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        float damage = 0.5f;
        if (!entity.getWorld().isClient)
            entity.damage((ServerWorld) entity.getWorld(), this.getDamageSources().thrown(this, this.getOwner()), damage);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.getWorld().isClient) {
            ServerWorld world = (ServerWorld) this.getWorld();
            boolean placed = place(world, this.getBlockPos());
            if (!placed)
                if (this.getMovementDirection() == Direction.SOUTH)
                    placed = place(world, this.getBlockPos().add(0, 0, -1));
                else if (this.getMovementDirection() == Direction.WEST)
                    placed = place(world, this.getBlockPos().add(-1, 0, 0));
            if (!placed) {
                BlockPos pos = this.getBlockPos();
                ItemEntity torchEntity = new ItemEntity(
                        world,
                        pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                        new ItemStack(Items.TORCH)
                );
                world.spawnEntity(torchEntity);
            }
            world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
        super.onCollision(hitResult);
    }

    /*
     * No more default
     * */
    private boolean canPlaceAt(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.AIR) ||
                world.getBlockState(pos).isOf(Blocks.SHORT_GRASS) ||
                world.getBlockState(pos).isOf(Blocks.SHORT_DRY_GRASS) ||
                world.getBlockState(pos).isOf(Blocks.WATER);
    }

    private boolean place(World world, BlockPos pos) {
        boolean placed = false;
        if (canPlaceAt(world, pos)) {
            Direction[] directions = Direction.values();
            for (Direction dir : directions) {
                if (dir.getAxis().isHorizontal() && WallTorchBlock.canPlaceAt(world, pos, dir)) {
                    world.setBlockState(
                            pos,
                            Blocks.WALL_TORCH.getDefaultState()
                                    .with(Properties.HORIZONTAL_FACING, dir)
                    );
                    placed = true;
                    break;
                }
            }

            if (!placed && Blocks.TORCH.getDefaultState().canPlaceAt(world, pos)) {
                world.setBlockState(pos, Blocks.TORCH.getDefaultState());
                placed = true;
            }
        }
        return placed;
    }
}

