package tools.starcitizen.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tools.starcitizen.entity.product.ProductEntity;
import tools.starcitizen.entity.vo.product.JsonProductVO;
import tools.starcitizen.entity.vo.product.ProductRentShopVO;
import tools.starcitizen.entity.vo.product.ProductShopVO;
import tools.starcitizen.entity.vo.product.ProductVO;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    ProductVO voCopy(ProductVO vo);

    ProductEntity entityCopy(ProductEntity entity);
    ProductVO entityToVO(ProductEntity entity);

    @Mapping(target = "canBuy", ignore = true)
    @Mapping(target = "canSell", ignore = true)
    @Mapping(target = "canRent", ignore = true)
    @Mapping(target = "commodity", ignore = true)
    JsonProductVO voToJsonVO(ProductVO vo);

    ProductShopVO voToShopVO(ProductVO vo);
    ProductRentShopVO voToRentShopVO(ProductVO vo);

}
